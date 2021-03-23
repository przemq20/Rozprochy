package server

import client.ConnectedClient

import java.net.{ DatagramPacket, DatagramSocket }
import java.util
import scala.collection.mutable.ListBuffer

//class provides UDP server support
class UdpServer(datagramSocket: DatagramSocket) extends Runnable {
  override def run(): Unit = {
    while (true) {
      val receiveBuffer = new Array[Byte](1024)
      util.Arrays.fill(receiveBuffer, 0.toByte)

      //receive message from client
      val receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length)
      datagramSocket.receive(receivePacket)
      val userPort = receivePacket.getPort

      //send received messages to the rest clients
      val data = new String(receivePacket.getData)
      //recognize client that send message by his port
      val client = Server.usersThreads.filter(_.udpPort == userPort).head
      println(s"Received message by UDP from port ${client.udpPort}-${client.nick}: $data ")
      val message = s"${client.nick} is writing: $data"
      sendPacketToAll(userPort, message)
    }
  }

  //method is sending provided message to all clients except that with provided port
  def sendPacketToAll(userPort: Int, message: String): Unit = {
    val sendBuffer = message.getBytes
    for {client <- Server.clientsToSend(userPort)} {
      //create packet and send
      val sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, client.inetAddress, client.udpPort)
      datagramSocket.send(sendPacket)
    }
  }
}