package server

import server.calculator.CalculatorServer
import server.stream.StreamTesterServer
import server.wordCounter.WordCounterServer

import scala.io.StdIn.readLine

class ServerMain(cPort: Int, sPort: Int, wPort:Int) {
  val calculatorServer   = new CalculatorServer(cPort)
  val streamTesterServer = new StreamTesterServer(sPort)
  val wordCounterServer = new WordCounterServer(wPort)
  scribe.info(s"Starting CalculatorServer at port $cPort")
  scribe.info(s"Starting StreamTesterServer at port $sPort")
  scribe.info(s"Starting WordCounterServer at port $wPort")
  calculatorServer.start()
  streamTesterServer.start()
  wordCounterServer.start()
  calculatorServer.waitForShutdown()
  streamTesterServer.waitForShutdown()
  wordCounterServer.waitForShutdown()
}

object ServerMain extends App {
  println("Enter port for CalculatorServer: (default: 50051)")
  val line  = readLine()
  val cPort = if (line.isEmpty) Some(50051) else line.toIntOption
  println("Enter port for StreamTesterServer: (default: 50052)")
  val line1 = readLine()
  val sPort = if (line1.isEmpty) Some(50052) else line1.toIntOption
  println("Enter port for WordCounterServer: (default: 50053)")
  val line2 = readLine()
  val wPort = if (line1.isEmpty) Some(50053) else line1.toIntOption

  if (cPort.isDefined && sPort.isDefined && wPort.isDefined)
    new ServerMain(cPort.get, sPort.get, wPort.get)
  else
    scribe.error("Bad arguments")
}
