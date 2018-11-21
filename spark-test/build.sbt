name := "spark-test"
version := "0.1"

scalaVersion := "2.11.11"

parallelExecution in Test := false

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.3.0"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.3.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5"