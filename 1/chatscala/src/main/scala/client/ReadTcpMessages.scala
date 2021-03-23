package client

import java.io.BufferedReader
import java.net.SocketException

//Class that creates a thread responsible for reading messages send via TCP
class ReadTcpMessages(in: BufferedReader) extends Runnable {
  override def run(): Unit = {
    try {
      while (true) {
        receiveMessage()
      }
    } catch {
      case _: SocketException =>
        println("Oh no! Something is wrong with the server!")
        println("Shutting down...")
        System.exit(0)
    }

    //Method that are reading message when it comes, parsing it and printing to screen
    def receiveMessage(): Unit = {
      val msg = in.readLine()
      msg.take(1) match {
        //if first letter of message is "E" it is an error send by the server
        case "E" =>
          val error = msg.drop(2).toInt
          error match {
            //Error 101 - client with this nickname was connected before
            case 101 =>
              println("Choose different nickname!")
              System.exit(-1)
            case _ => println("Unrecognized error")
          }
        //if first letter of message is "M" it is a message
        case "M" => println(msg.drop(2))
        case _ => println(msg)
      }
    }
  }
}


