package client

import server.Config

import java.io.{ BufferedReader, InputStreamReader, PrintWriter }
import java.net.{ DatagramSocket, InetAddress, MulticastSocket, Socket }
import java.util.UUID
import scala.io.StdIn.readLine

//Main Class of Client
object Client extends App {
  val serverPort = Config.serverPort
  val uuid: UUID = UUID.randomUUID()

  //Parameters for multicast
  val multicastSocket = new MulticastSocket(Config.multicastPort)
  multicastSocket.joinGroup(InetAddress.getByName(Config.multicastChannel))

  //starting client and picking nickname
  println("Client is starting")
  println("Provide your nickname:")
  val name = readLine
  println(
    s"""Hello $name! You can enter your message.
       |If you precede yours message with "U " - it will be send via UDP (normally it is send via TCP).
       |If you precede yours message with "M " - it will be send using UDP multicast.
       |""".stripMargin)

  //creating new TCP and UDP sockets
  val socket = new Socket("localhost", serverPort)
  val clientPort = socket.getLocalPort
  val out = new PrintWriter(socket.getOutputStream, true)
  val in = new BufferedReader(new InputStreamReader(socket.getInputStream))

  val address = InetAddress.getLocalHost
  val datagramSocket = new DatagramSocket(clientPort)
  sendInfo()

  //starting threads responsible for reading and sending message
  new Thread(new ReadTcpMessages(in)).start()
  new Thread(new ReadUdpMessages(datagramSocket)).start()
  new Thread(new ReadMulticastMessages(multicastSocket)).start()
  new Thread(new SendMessages(out, address, serverPort, datagramSocket, name)).start()

  //function that sends info about client to TCP server
  def sendInfo(): Unit = {
    val port: Int = datagramSocket.getLocalPort
    out.println(s"$uuid $name $port")
  }
}
