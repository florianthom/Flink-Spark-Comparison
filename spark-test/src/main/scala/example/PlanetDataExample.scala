package example

import java.io.File
import scala.io.Source
import scala.collection.mutable.Queue
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{StreamingContext, Seconds}
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel, StreamingKMeans}
import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import com.github.tototoshi.csv._

object PlanetDataExample
{
  val numClusters = 5
  val numDim = 3
  val numIter = 10
  
  def parseCSV(line: String): List[String] =
  {
    val source = Source.fromString(line)
    val reader = CSVReader.open(source)
    reader.all()(0)
  }
  
  def containsEmpty(attr: List[String]): Boolean =
  {
    attr.filter(p => p.isEmpty()).length > 0
  }
  
  def transformToTuple(attr: List[String]): (Integer, String, Double, Double, Double, Double, Double, Double, Double, Double) =
  {
    (
      attr(0).toInt,
      attr(1),
      attr(2).toDouble,
      attr(3).toDouble,
      attr(4).toDouble,
      attr(5).toDouble,
      attr(6).toDouble,
      attr(7).toDouble,
      attr(8).toDouble,
      attr(9).toDouble
    )
  }
  
  def run(spark: SparkSession, path: String) =
  {
    val sc = spark.sparkContext
    val ssc = new StreamingContext(sc, Seconds(2))
    
    var predicted: RDD[(Vector, Int)] = sc.emptyRDD
    var kmodel = new KMeansModel(Array.fill(numClusters)(Vectors.zeros(numDim)))
    
    val relPath = new File(path)
    val data = ssc.textFileStream("file://" + relPath.getCanonicalPath)
      .map(parseCSV(_))
      .filter(!containsEmpty(_))
      .map(transformToTuple(_))
      
    val vectors = data
      .map(f => Vectors.dense(f._4, f._5, f._7))
      
    vectors.foreachRDD(rdd => {
      kmodel = new KMeans().setK(numClusters).setInitialModel(kmodel).setMaxIterations(numIter).run(rdd)
      val current = rdd.map(f => (f, kmodel.predict(f)))
      predicted = predicted.union(current)
    })
    
    ssc.start()
    ssc.awaitTerminationOrTimeout(20000)
    ssc.stop(false)
    
    val export = predicted.map(f => (f._1(0), f._1(1), f._1(2), f._2)).collect().toList
    
    ExportData.AsCsv(
      "ClusterPlanetData.csv",
      List("planet_mass", "planet_radius", "distance", "cluster"),
      export.map(f => List(f._1.toString(), f._2.toString(), f._3.toString(), f._4.toString()))
    )
  }
}