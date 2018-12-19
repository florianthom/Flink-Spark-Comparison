package example

import java.io.File
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{StreamingContext, Seconds}

object WordCountExample
{
  def split(line: String): Array[String] =
  {
    line.toLowerCase().replaceAll(",", " ").replaceAll(".", " ").split(" ")
  }
  
  def run(spark: SparkSession, path: String) =
  {
    val sc = spark.sparkContext
    val ssc = new StreamingContext(sc, Seconds(2))
    
    var counts: RDD[(String, Long)] = sc.emptyRDD
    
    val relPath = new File(path)
    val data = ssc.textFileStream("file://" + relPath.getCanonicalPath)
      .filter(!_.isEmpty())
      .flatMap(split(_))
      
      
    val words = data.map(f => (f, 1L)).reduceByKey((a, b) => a + b)
    words.foreachRDD(rdd => {
      val merged = counts.union(rdd)
      counts = merged.reduceByKey((a, b) => a + b)
    })
    
    
    ssc.start()
    ssc.awaitTerminationOrTimeout(20000)
    ssc.stop(false)
    
    val export = counts.collect().toList
    
    ExportData.AsCsv(
      "WordCountData.csv",
      List("word", "count"),
      export.map(f => List(f._1, f._2.toString()))
    )
  }
}