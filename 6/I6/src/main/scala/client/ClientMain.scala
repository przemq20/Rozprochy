package client

import arithmetic.ArithmeticOpArguments
import calculator.CalculatorGrpc
import io.grpc.{ ManagedChannel, ManagedChannelBuilder }
import streamingProto.{ Request, StreamTesterGrpc }
import wordCounter.{ WordCountGrpc, Words }

import java.net.ConnectException
import scala.io.StdIn.readLine
import scala.jdk.CollectionConverters._

class ClientMain(remoteHost: String, port: Int) {
  val channel: ManagedChannel = ManagedChannelBuilder
    .forAddress(remoteHost, port)
    .usePlaintext()
    .build()

  val calcNonBlockingStub:         CalculatorGrpc.CalculatorStub         = CalculatorGrpc.newStub(channel)
  val calcBlockingStub:            CalculatorGrpc.CalculatorBlockingStub = CalculatorGrpc.newBlockingStub(channel)
  val streamTesterNonBlockingStub: StreamTesterGrpc.StreamTesterStub     = StreamTesterGrpc.newStub(channel)
  val wordCounterNonBlockingStub:  WordCountGrpc.WordCountStub           = WordCountGrpc.newStub(channel)

  def run(): Unit = {
    while (true) {
      println("""Choose Action:
                |1 - Generate Prime Numbers
                |2 - Add two numbers
                |3 - Subtract two numbers
                |4 - Count words
                |5 - exit
                |""".stripMargin)

      readLine match {
        case "1" =>
          println("Provide lower bound:")
          val lowerBound = readLine().toIntOption
          println("Provide upper bound:")
          val upperBound = readLine().toIntOption
          if (lowerBound.isDefined && lowerBound.get > 0 && upperBound.isDefined && upperBound.get > lowerBound.get) {
            try {
              val request = Request.newBuilder().setLowerBound(lowerBound.get).setUpperBound(upperBound.get).build()
              streamTesterNonBlockingStub.generatePrimeNumbers(request, new Observers.PrimeNumbersObserver)
            } catch {
              case e: ConnectException => scribe.error(e.getMessage)
              case _ => scribe.error("Error during executing prime numbers generation")
            }
          } else scribe.error("wrong arguments")
        case "2" =>
          println("What two numbers do you want to add? (ex: 10 11)")
          val args = readLine().split(" ").map(_.toIntOption)
          if (args.length == 2 && args.forall(_.isDefined)) {
            try {
              calcNonBlockingStub
                .add(
                  ArithmeticOpArguments
                    .newBuilder()
                    .setArg1(args(0).get)
                    .setArg2(args(1).get)
                    .build(),
                  new Observers.ArithmeticObserver
                )
            } catch {
              case e: ConnectException => scribe.error(e.getMessage)
              case _ => scribe.error("Error during executing addition")
            }
          } else scribe.error("Bad arguments")
        case "3" =>
          println("What two numbers do you want to subtract? (ex: 11 10)")
          val args = readLine().split(" ").map(_.toIntOption)
          if (args.length == 2 && args.forall(_.isDefined)) {
            try {
              println(s"""Result: ${calcBlockingStub
                .subtract(
                  ArithmeticOpArguments
                    .newBuilder()
                    .setArg1(args(0).get)
                    .setArg2(args(1).get)
                    .build()
                )
                .getRes}""")
            } catch {
              case e: ConnectException => scribe.error(e.getMessage)
              case _ => scribe.error("Error during executing subtraction")
            }
          } else scribe.error("Bad arguments")
        case "4" =>
          println("Write your text: ")
          val words    = readLine().split(" ").filterNot(_.isEmpty)
          val wordList = Words.newBuilder().addAllValues(Iterable.newBuilder.addAll(words).result().asJava).build()

          wordCounterNonBlockingStub.countWord(
            wordList,
            new Observers.ResultObserver
          )
        case "5" => System.exit(0)
        case _   => scribe.error("Unrecognized Argument")
      }
    }
  }
}

object ClientMain extends App {
  println("Enter port: (default: 50050)")
  val line = readLine()
  val port = if (line.isEmpty) Some(50050) else line.toIntOption
  if (port.isDefined)
    new ClientMain("localhost", port.get).run()
  else scribe.error("Bad argument")
}
