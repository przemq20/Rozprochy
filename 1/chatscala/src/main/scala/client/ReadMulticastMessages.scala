package client

import java.net.{ DatagramPacket, MulticastSocket }

//Class that creates a thread responsible for reading messages send via multicast socket
class ReadMulticastMessages(socket: MulticastSocket) extends Runnable {
  override def run(): Unit = {

    while (true) {
      //receive message
      val buf = new Array[Byte](1000)
      val recv = new DatagramPacket(buf, buf.length)
      socket.receive(recv)
      //parse received message
      val data = new String(recv.getData).split(" ")
      val nick = if (data.nonEmpty) data(0) else ""
      val msg = if (data.size > 1) new String(recv.getData).drop(nick.length + 1) else ""

      //if client nick is the sane as this in message - ignore, otherwise print
      if (nick != Client.name) println(s"Received multicast message from $nick: $msg")
    }
  }
}