package example

import org.apache.flink.streaming
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.assigners.SlidingTimeWindows
import scala.tools.jline_embedded.internal.Configuration
import org.apache.flink.configuration.ConfigConstants
import java.nio.charset.StandardCharsets
import scala.util.parsing.json.JSON

// nc -lk 9000


class PrintIfTextSmall(){
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val text: DataStream[String] = env.socketTextStream("127.0.0.1", 9000, '\n')
    text.filter(a=>a.length()<5).print()
    env.execute()
}