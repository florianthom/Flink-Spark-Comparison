package example

import org.apache.flink.streaming
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time



//import org.apache.flink.examples.scala.clustering
//import org.apache.flink.streaming.api.scala.windowing.Delta
//import org.apache.flink.streaming.api.windowing.helper.Time
//import org.apache.flink.util.Collector

//object StreamingExample extends App{
//  //val data = sc.textFile("../data/adult.data")
//  
//  val a = Processing.degrees
//  val b = Processing.race
//  
////  val vectors = data
////    .map(f => Processing.extractAttributes(f, List(3, 8)))
////    .map(f => Processing.vectorizeAttributes(f, List(a, b)))
//  
//  val numClusters = 2
//  val numInterations = 20
//
//  //val t = KMeans
//
//  //val WSSSE = clusters.computeCost(vectors)
//  //println("Within Set sum of Squared Errors = " + WSSSE)
//}








//case class WordWithCount(word: String, count: Long)

object StreamingExample extends App{
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment   // StreamExecutionEnvironment.createRemoteEnvironment("192.168.56.160", 6123);
    val text: DataStream[String] = env.socketTextStream("127.0.0.1", 9000, '\n')                  //env.fromElements(1.toString(),2.toString(),3.toString())
    text.filter(a=>a.length()<5).print()         // .flatMap { w => w.split("\\s") }.map { w => WordWithCount(w, 1) }.keyBy("word").timeWindow(Time.seconds(5)).sum("count").print() //.setParallelism(1) // single threaded
    env.execute()
}