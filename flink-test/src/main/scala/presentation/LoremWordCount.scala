package presentation

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
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import org.apache.flink.streaming.api.functions.sink.DiscardingSink


case class WordWithCount(word:String,count:Long)

class LoremWordCount {
      val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment   // StreamExecutionEnvironment.createRemoteEnvironment("192.168.56.160", 6123);
      env.setParallelism(4)
      val text: DataStream[String] = env.readTextFile("/home/florian/Desktop/FlinkSparkComparison/Flink-Spark-Comparison/data/lorem/2000size",StandardCharsets.UTF_8.name())
      text
        .flatMap(line => line
            .toLowerCase()
            .replaceAll(",", " ")
            .replaceAll(".", " ")
            .split("\\s"))
        .map(w => WordWithCount(w, 1))
        .keyBy("word")
        .sum("count")
        
        .addSink(new DiscardingSink[WordWithCount]()) //.setParallelism(1) // single threaded
      env.execute()
}

class Sink[T] extends SinkFunction[T] {
}

      //val text: DataStream[String] = env.readTextFile("../data/lipsum_short.txt",StandardCharsets.UTF_8.name())
