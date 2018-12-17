//map wiht state
//checkpoints
//savepoints
//watermarks
//global windows,tumbling windows, sliding windows, session windows, count windows

package main

import example.ReadJSONFromFile
import presentation.LoremWordCount

object Main extends App{
  val startTime = System.nanoTime
  new LoremWordCount()
  val endTime = System.nanoTime
  println("sdfgasddggsd")
  println((endTime.toDouble - startTime.toDouble) / 1000000000.toDouble)
}

