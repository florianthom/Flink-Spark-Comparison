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
    .set("spark.executor.memory","2g")
    .set("spark.storage.memoryFraction","0.8")
    .set("spark.driver.memory", "2g")
  val spark = SparkSession.builder().appName("SparkTest").config(conf).getOrCreate()
  val sc = spark.sparkContext
  
  //+++++++++ HACKY
  val movey = new Thread {
    override def run {
      Thread.sleep(4000)
      "touch ../data/compositepars_short_nohead.csv".run()
      "mv ../data/compositepars_short_nohead.csv ../data/streaming/compositepars_moved_short_nohead.csv".run()
    }
  }
  
  movey.start()
  println("MOVEY STARTED")
  
  val planetData = EvaluatePlanetDataStream.run(spark)
  //val adultData = EvaluateAdultData.run(spark)

  sc.stop()
  
  ExportData.AsCsv(
    "ClusterPlanetData.csv",
    List("planet_mass", "planet_radius", "distance", "cluster"),
    planetData.map(f => List(f._1.toString(), f._2.toString(), f._3.toString(), f._4.toString()))
  )
  
  movey.join()
  "mv ../data/streaming/compositepars_moved_short_nohead.csv ../data/compositepars_short_nohead.csv".run()
  
  //ExportData.AsCsv(
  //    "ClusterAdultData.csv",
  //    List("Race", ...
  //    adultData.map(f => List(f._1.toString(), f._2.toString(), f._3.toString()))
  //)

}
