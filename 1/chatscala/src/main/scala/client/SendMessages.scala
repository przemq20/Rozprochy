package client

import java.io.PrintWriter
import java.net.{ DatagramPacket, DatagramSocket, InetAddress, SocketException }
import scala.io.StdIn.readLine

//Class that creates a thread responsible for sending messages by all channels
class SendMessages(out: PrintWriter, address: InetAddress, serverPort: Int, datagramSocket: DatagramSocket, nick: String) extends Runnable {
  override def run(): Unit = {
    try {
      while (true) {
        val msg = readLine
        //Depending on first letter of provided message send via suitable channel
        msg.split(" ").head match {
          case "U" => sendUdp(msg.drop(2))
          case "M" => sendMulticast(msg.drop(2))
          case _ => sendTcp(msg)
        }
      }
    } catch {
      //catch exceptions
      case _: SocketException =>
        println("Oh no, we cant send this message, restarting thread")
        new Thread(new SendMessages(out,address,serverPort,datagramSocket,nick)).start()
    }
  }

  //Method responsive for sending message via UDP socket
  def sendUdp(msg: String): Unit = {
    val sendBuffer = msg.getBytes
    val sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, serverPort)
    datagramSocket.send(sendPacket)
  }

  //Method responsive for sending message via TCP socket
  def sendTcp(msg: String): Unit = out.println(msg)

  //Method responsive for sending message via UDP multicast
  def sendMulticast(msg: String): Unit = {
    //add nick to message to recognize sender
    val message = s"$nick $msg"
    val hi = new DatagramPacket(message.getBytes, message.length, InetAddress.getByName("228.5.6.7"), 6789)
    datagramSocket.send(hi)
  }
}
