package example

import scala.collection.mutable.Queue
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StructType, StructField}
import org.apache.spark.sql.types.{IntegerType, DoubleType, StringType}
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{StreamingContext, Seconds}
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel, StreamingKMeans}
import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.hadoop.io.{Text, LongWritable}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import com.github.tototoshi.csv._
import scala.io.Source
import java.io.File

object EvaluatePlanetDataStream
{
  val numClusters = 3
  val numDim = 3
  val model = new StreamingKMeans().setK(numClusters).setDecayFactor(1d).setRandomCenters(numDim, 0.0)
  
  def parseCSV(line: String): (Integer, String, Double, Double, Double, Double, Double, Double, Double, Double) = {
    val source = Source.fromString(line)
    val reader = CSVReader.open(source)
    val parsed = reader.all()(0).map(f => if(f.isEmpty()) "0" else f)
    
    print(parsed(1))
    (
        parsed(0).toInt,
        parsed(1),
        parsed(2).toDouble,
        parsed(3).toDouble,
        parsed(4).toDouble,
        parsed(5).toDouble,
        parsed(6).toDouble,
        parsed(7).toDouble,
        parsed(8).toDouble,
        parsed(9).toDouble
    )
  }
  
  def run(spark: SparkSession): List[(Double, Double, Double, Int)] =
  {
    //ID, Planet Name, Orbital Period [days], Planet Mass or M*sin(i) [Earth mass], Planet Radius [Earth radii], Planet Density [g/cm**3], Distance [pc]
    //Stellar Mass [Solar mass], Stellar Radius [Solar radii], Stellar Age [Gyr]
   
    //loc_rowid,fpl_name,fpl_orbper,fpl_bmasse,fpl_rade,fpl_dens,fst_dist,fst_mass,fst_rad,fst_age
    //1,11 Com b,326.03000000,6165.60000,12.100,19.10000,93.37,2.70,19.00,
    val sc = spark.sparkContext
    
    val queueVectors = Queue[RDD[Vector]]()
    val queuePred = Queue[RDD[Int]]()
    
    val ssc = new StreamingContext(sc, Seconds(2))
    
    val relPath = new File("./../data/streaming")
    println("LOOKING FOR DATA AT " + relPath.getCanonicalPath)
    val data = ssc.textFileStream("file://" + relPath.getCanonicalPath).map(parseCSV(_))
    //val data = ssc.fileStream[LongWritable, Text, TextInputFormat](relPath.getCanonicalPath).map(f => f._2.toString()).map(parseCSV(_))
    
    
    val vectors = data
      .map(f => Vectors.dense(f._4, f._5, f._7))
    
    model.trainOn(vectors)
    
    val predictions = model.predictOn(vectors)
    
    vectors.foreachRDD(rdd => queueVectors += rdd)
    predictions.foreachRDD(rdd => queuePred += rdd)
    
    ssc.start()
    ssc.awaitTerminationOrTimeout(20000)
    ssc.stop(false)
    
    val rddVectors = queueVectors.reduce((a, b) => a.union(b))
    val rddPred = queuePred.reduce((a, b) => a.union(b))
    
    rddVectors.zip(rddPred).map(f => (f._1(0), f._1(1), f._1(2), f._2)).collect().toList
    
    //rddVectors.map(f => (f(0), f(1), f(2), )).collect().toList
  }
}