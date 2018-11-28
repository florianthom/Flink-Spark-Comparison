//39, State-gov, 77516, Bachelors, 13, Never-married, Adm-clerical, Not-in-family, White, Male, 2174, 0, 40, United-States, <=50K

package example

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg
import breeze.linalg._
import breeze.plot._

object StreamingExample extends App 
{
  val conf= new SparkConf()
    .setMaster("local[4]")
    .setAppName("StreamingExample")
    .set("spark.executor.memory","4g")
    .set("spark.storage.memoryFraction","0.8")
    .set("spark.driver.memory", "2g")
  val spark = SparkSession.builder().appName("SparkTest").config(conf).getOrCreate()
  val sc = spark.sparkContext
  
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
  
  val all = vectors.map(f => (f(0), f(1), clusters.predict(f))).collect().toList

  sc.stop()
  
  println("Within Set sum of Squared Errors = " + WSSSE)
  
  val f = Figure()
  val p = f.subplot(0)
  
  for(a <- all.zipWithIndex)
  {
    
    val index = a._2.toDouble
    val attrA = a._1._1
    val attrB = a._1._2.toInt
    val cluster = a._1._3
    
    val x = DenseVector.apply(index)
    val y = DenseVector.apply(attrA)
    
    val r = (attrB * 17) % 255
    val g = (attrB * 5) % 255
    val b = (attrB * 3) % 255
    val color = r + "," + g + "," + b
    
    val style = Array('.', '+', 'o')(cluster)
    
    p += plot(x = x, y = y, style = style, colorcode = color)
  }

  println(all)
  
  f.saveas("clusters.png")
  
}
