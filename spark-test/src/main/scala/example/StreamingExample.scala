//39, State-gov, 77516, Bachelors, 13, Never-married, Adm-clerical, Not-in-family, White, Male, 2174, 0, 40, United-States, <=50K

package example

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import sys.process._

object StreamingExample extends App 
{
  val conf= new SparkConf()
    .setMaster("local[4]")
    .setAppName("StreamingExample")
    .set("spark.executor.memory","4g")
    .set("spark.storage.memoryFraction","0.8")
    .set("spark.driver.memory", "4g")
  val spark = SparkSession.builder().appName("SparkTest").config(conf).getOrCreate()
  val sc = spark.sparkContext
  
  //+++++++++ HACKY
  val movey = new Thread {
    override def run {
      Thread.sleep(4000)
      "touch ../data/lorem/2000size".run()
      "mv ../data/lorem/2000size ../data/streaming/lipsum_moved_short.txt".run()
    }
  }
  
  movey.start()
  println("MOVEY STARTED")
  
  // val planetData = EvaluatePlanetDataStream.run(spark)
  //val adultData = EvaluateAdultData.run(spark)
  
  WordCountExample.run(spark, "../data/streaming")

  sc.stop()
  
  movey.join()
  "mv ../data/streaming/lipsum_moved_short.txt ../data/lorem/2000size".run()
  
  
  /*
  ExportData.AsCsv(
    "ClusterPlanetData.csv",
    List("planet_mass", "planet_radius", "distance", "cluster"),
    planetData.map(f => List(f._1.toString(), f._2.toString(), f._3.toString(), f._4.toString()))
  )
  * */

  
  //ExportData.AsCsv(
  //    "ClusterAdultData.csv",
  //    List("Race", ...
  //    adultData.map(f => List(f._1.toString(), f._2.toString(), f._3.toString()))
  //)

}
