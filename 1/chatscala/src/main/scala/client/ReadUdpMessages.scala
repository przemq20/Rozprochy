package client

import java.net.{ DatagramPacket, DatagramSocket }
import java.util

//Class that creates a thread responsible for reading messages send via UDP
class ReadUdpMessages(datagramSocket: DatagramSocket) extends Runnable {
  override def run(): Unit = {
    val receiveBuffer = new Array[Byte](1024)
    while (true) {
      receiveMessage()
    }

    //Method that are reading message when it comes, parsing it and printing to screen
    def receiveMessage(): Unit = {
      util.Arrays.fill(receiveBuffer, 0.toByte)
      val receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length)
      datagramSocket.receive(receivePacket)
      val msg = new String(receivePacket.getData)
      println(msg)
    }
  }
}