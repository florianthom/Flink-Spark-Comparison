package example

import org.apache.spark.sql.SparkSession
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.{Vector, Vectors}

object EvaluatePlanetData
{
  def run(spark: SparkSession): List[(Double, Double, Double, Int)] =
  {
    //ID, Planet Name, Orbital Period [days], Planet Mass or M*sin(i) [Earth mass], Planet Radius [Earth radii], Planet Density [g/cm**3], Distance [pc]
    //Stellar Mass [Solar mass], Stellar Radius [Solar radii], Stellar Age [Gyr]
    
    //loc_rowid,fpl_name,fpl_orbper,fpl_bmasse,fpl_rade,fpl_dens,fst_dist,fst_mass,fst_rad,fst_age
    //1,11 Com b,326.03000000,6165.60000,12.100,19.10000,93.37,2.70,19.00,
    val csv = spark.read.format("csv").option("header", "true").load("../data/compositepars_short.csv").rdd
    val data = csv.map(f => (
        if (f.isNullAt(0)) 0 else f.getString(0).toInt,
        if (f.isNullAt(1)) "" else f.getString(1),
        if (f.isNullAt(2)) 0d else f.getString(2).toDouble,
        if (f.isNullAt(3)) 0d else f.getString(3).toDouble,
        if (f.isNullAt(4)) 0d else f.getString(4).toDouble,
        if (f.isNullAt(5)) 0d else f.getString(5).toDouble,
        if (f.isNullAt(6)) 0d else f.getString(6).toDouble,
        if (f.isNullAt(7)) 0d else f.getString(7).toDouble,
        if (f.isNullAt(8)) 0d else f.getString(8).toDouble,
        if (f.isNullAt(9)) 0d else f.getString(9).toDouble
    ))
  
    val a = ProcessingAdultData.degrees
    val b = ProcessingAdultData.race
    
    val vectors = data
      .map(f => Vectors.dense(f._4, f._5, f._7))
      .cache()
    
    val numClusters = 3
    val numInterations = 10
    val clusters = KMeans.train(vectors, numClusters, numInterations)
    
    val WSSSE = clusters.computeCost(vectors)
    
    println("Within Set sum of Squared Errors = " + WSSSE)
    
    vectors.map(f => (f(0), f(1), f(2), clusters.predict(f))).collect().toList
  }
}