package example

import org.apache.flink.streaming
//import org.apache.flink.examples.scala.clustering
//import org.apache.flink.streaming.api.scala.windowing.Delta
//import org.apache.flink.streaming.api.windowing.helper.Time
//import org.apache.flink.util.Collector

object StreamingExample extends App{
  //val data = sc.textFile("../data/adult.data")
  
  val a = Processing.degrees
  val b = Processing.race
  
//  val vectors = data
//    .map(f => Processing.extractAttributes(f, List(3, 8)))
//    .map(f => Processing.vectorizeAttributes(f, List(a, b)))
  
  val numClusters = 2
  val numInterations = 20

  //val t = KMeans

  //val WSSSE = clusters.computeCost(vectors)
  //println("Within Set sum of Squared Errors = " + WSSSE)
}