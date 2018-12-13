package example

import org.apache.flink.streaming
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.assigners.SlidingTimeWindows
import scala.tools.jline_embedded.internal.Configuration
import org.apache.flink.configuration.ConfigConstants
import java.nio.charset.StandardCharsets
import scala.util.parsing.json.JSON

case class WordWithOne(word:String,count:Long)

class SlidingWindowStandard {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment   // StreamExecutionEnvironment.createRemoteEnvironment("192.168.56.160", 6123);
    val text: DataStream[String] = env.socketTextStream("127.0.0.1", 9000, '\n')                  //env.fromElements(1.toString(),2.toString(),3.toString())

    //val slidingCnts = text.map(a=>(a,1)).keyBy(0).timeWindow(Time.seconds(5)).sum(1).print()
    //val slidingCnts = text.map(a=>WordWithOne(a,1)).keyBy("word").timeWindow(Time.seconds(5)).
    //val test = text.map(a=>(a,1)).keyBy(b=>b._1).timeWindow(Time.seconds(5)).fold(0)((acc,v) => acc + v._2).print()
    //val test = text.map(a=>(a,1)).keyBy(b=>b._1).timeWindow(Time.seconds(5)).reduce((c,d) => (c._1,c._2+d._2)).print()
    val test = text.map(a=>(a,1)).keyBy(b=>b._1).timeWindow(Time.seconds(5)).reduce((c,d) => (c._1,c._2+d._2)).print()

   env.execute()
}