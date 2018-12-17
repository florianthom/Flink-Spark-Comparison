// benchmark: https://data-artisans.com/blog/curious-case-broken-benchmark-revisiting-apache-flink-vs-databricks-runtime

package example

import org.apache.flink.streaming
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time


// nc -lk 9000

case class WordWithCount(word:String,count:Long)


class CountWords(){
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment  
    //env.setMaxParallelism()
    val text: DataStream[String] = env.socketTextStream("127.0.0.1", 9000, '\n') 
 
    // count number of processed elements
    //text.map(a=>(1,1)).keyBy(0).sum(0).map(b=>b._1).rebalance.print()
    text.map(a=>(1,1)).keyBy(0).sum(0).map(b=>b._1).print()

    
    
    // with window and each String in line and grouped by String
    //val result = text.flatMap { w => w.split("\\s") }.map { w => WordWithCount(w, 1) }.keyBy("word").timeWindow(Time.seconds(5)).sum("count").print() //.setParallelism(1) // single threaded
    
    env.execute()

}