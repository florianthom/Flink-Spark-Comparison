package example

import org.apache.flink.streaming
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time


// nc -lk 9000

case class WordWithCount(word:String,count:Long)


class CountWords(){
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment  
    val text: DataStream[String] = env.socketTextStream("127.0.0.1", 9000, '\n') 
 
    // with window and each String in line and grouped by String
    val result = text.flatMap { w => w.split("\\s") }.map { w => WordWithCount(w, 1) }.keyBy("word").timeWindow(Time.seconds(5)).sum("count").print() //.setParallelism(1) // single threaded
    
    env.execute()

}