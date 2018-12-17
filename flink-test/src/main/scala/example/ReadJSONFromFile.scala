package example

import org.apache.flink.streaming
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.assigners.SlidingTimeWindows
import scala.tools.jline_embedded.internal.Configuration
import org.apache.flink.configuration.ConfigConstants
import java.nio.charset.StandardCharsets
import scala.util.parsing.json.JSON
import org.apache.flink.streaming.api.datastream.DataStreamUtils
import scala.collection.JavaConverters.asScalaIteratorConverter



  // keySet: Set(relativeStart, filesize, eventType, ip, duid, suspicious, hostname, clientState, validateStart, url, guid, timeEnd, scope, uuid, eventVersion, stateReason, usrdn, traceTimeentryUnix, traceIp, transferStart, version, appid, catStart, taskid, filename, usr, dataset, traceId, localSite, protocol, traceTimeentry, timeStart, remoteSite)
  // Unterscheided sich zwischen documenten


class ReadJSONFromFile {
      val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment   // StreamExecutionEnvironment.createRemoteEnvironment("192.168.56.160", 6123);
      //small
      //val text: DataStream[String] = env.readTextFile("/home/florian/Desktop/FlinkSparkComparison/Flink-Spark-Comparison/data/cernData/traces_2017-01_03_04", StandardCharsets.UTF_8.name())
      // big
      val text: DataStream[String] = env.readTextFile("/home/florian/Desktop/FlinkSparkComparison/Flink-Spark-Comparison/data/cernData/traces_2017-01_03_04", StandardCharsets.UTF_8.name())

      
      // Function to instantly map ALL Values to String
      // Breaks, because sometimes a value is "null", so with _,toString() we have to check for null first
      //val result: DataStream[Map[String,String]] = text.map(line => JSON.parseFull(line) match {case Some(x:Map[String,Any]) => x.mapValues(_.toString())})
      //result.print()
      
      
      //Function to convert only needed Fields to Specefic datatype
      val result : DataStream[Map[String,Any]] = text.map(line => JSON.parseFull(line) match {case Some(x:Map[String,Any]) => x})
      val result2 = result.map(a=>a.get("eventType").get)
      result2.print()
      
      env.execute()
}