package example

import org.apache.spark.sql.SparkSession
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg


object EvaluateAdultData
{
  def run(spark: SparkSession): List[(Double, Double, Int)] =
  {
    val sc = spark.sparkContext
    val data = sc.textFile("../data/adult_short.data")
  
    val a = ProcessingAdultData.degrees
    val b = ProcessingAdultData.race
    
    val vectors = data
      .map(f => ProcessingAdultData.extractAttributes(f, List(3, 8)))
      .map(f => ProcessingAdultData.vectorizeAttributes(f, List(a, b)))
      .cache()
    
    val numClusters = 2
    val numInterations = 10
    val clusters = KMeans.train(vectors, numClusters, numInterations)
    
    val WSSSE = clusters.computeCost(vectors)
    
    println("Within Set sum of Squared Errors = " + WSSSE)
    
    vectors.map(f => (f(0), f(1), clusters.predict(f))).collect().toList
  }
}