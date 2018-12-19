package main

import example._
import presentation._

object Main extends App{
  val startTime = System.nanoTime
  new LoremWordCount()
  val endTime = System.nanoTime
  println((endTime.toDouble - startTime.toDouble) / 1000000000.toDouble)
}

