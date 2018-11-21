name := "flink-test"
version := "0.1"

scalaVersion := "2.11.11"

parallelExecution in Test := false

libraryDependencies += "org.apache.flink" %% "flink-scala" % "1.6.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5"
