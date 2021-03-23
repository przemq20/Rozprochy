package server

import client.ConnectedClient

import java.net.{ DatagramSocket, ServerSocket }
import java.util.UUID
import scala.collection.mutable.ListBuffer

//Main server class
object Server extends App {
  //functions returning list of all clients except one with provided port or nick
  def clientsToSend(userPort: Int): ListBuffer[ConnectedClient] = Server.usersThreads.filterNot(_.udpPort == userPort)
  def clientsToSend(nick: String): ListBuffer[ConnectedClient] = Server.usersThreads.filterNot(_.nick == nick)
  def clientsToSend(id: UUID): ListBuffer[ConnectedClient] = Server.usersThreads.filterNot(_.tcpServer.id == id)

  //list of all connected clients
  val usersThreads: ListBuffer[ConnectedClient] = ListBuffer.empty
  val portNumber = Config.serverPort

  println("Server is starting")

  //create TCP and UDP sockets
  val serverSocket = new ServerSocket(portNumber)
  val datagramSocket = new DatagramSocket(portNumber)
  val udpServer = new UdpServer(datagramSocket)

  //create new threads to manage server and handle UDP socket
  new Thread(new ServerThread()).start()
  new Thread(udpServer).start()
  println("UDP server started")

  while (true) {
    val clientSocket = serverSocket.accept
    val id = UUID.randomUUID
    //create new Thread to handle Tcp Socket (one thread for one connected client)
    val tpcServer = new TcpServer(clientSocket, id)
    new Thread(tpcServer).start()
  }
}


