//39, State-gov, 77516, Bachelors, 13, Never-married, Adm-clerical, Not-in-family, White, Male, 2174, 0, 40, United-States, <=50K

package example

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg

object StreamingExample extends App 
{
  val conf= new SparkConf()
    .setMaster("local[4]")
    .setAppName("StreamingExample")
    .set("spark.executor.memory","4g")
    .set("spark.storage.memoryFraction","0.8")
    .set("spark.driver.memory", "2g")
  val sc= new SparkContext(conf)
  val data = sc.textFile("../data/adult_short.data")
  
  val a = Processing.degrees
  val b = Processing.race
  
  val vectors = data
    .map(f => Processing.extractAttributes(f, List(3, 8)))
    .map(f => Processing.vectorizeAttributes(f, List(a, b)))
    .cache()
  
  val numClusters = 2
  val numInterations = 10
  val clusters = KMeans.train(vectors, numClusters, numInterations)
  
  val WSSSE = clusters.computeCost(vectors)
  
  sc.stop()  

  println("Within Set sum of Squared Errors = " + WSSSE)
  
}
