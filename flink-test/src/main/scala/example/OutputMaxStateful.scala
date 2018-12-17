package example

import org.apache.flink.streaming
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time


class OutputMaxStateful {
      val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment  
    //env.setMaxParallelism()
    val numbers: DataStream[String] = env.socketTextStream("127.0.0.1", 9000, '\n') 
 
    numbers.keyBy(0).max(0)
    //env.execute()
}