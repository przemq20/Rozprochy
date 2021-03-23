package server

import client.ConnectedClient
import server.Server.clientsToSend

import java.io.{ BufferedReader, InputStreamReader, PrintWriter }
import java.net.{ Socket, SocketException }
import java.util.UUID

//class provides TCP server support
class TcpServer(val socket: Socket, val id: UUID) extends Runnable {
  val out = new PrintWriter(socket.getOutputStream, true)
  val in = new BufferedReader(new InputStreamReader(socket.getInputStream))

  override def run(): Unit = {
    val client = parseClient(in.readLine())
    //check if client with provided nick already exist
    if (Server.usersThreads.map(_.nick).contains(client.nick)) {
      //client with this nick is already connected - Error 101
      //message has prefix "E " because it's error message
      out.println("E 101")
    }
    else {
      printClientConnected(client.nick)
      printAlreadyConnectedClients()
      //add client to list of all clients
      Server.usersThreads.addOne(client)
      println(s"Client ${client.nick} connected")
      try {
        while (true) {
          receiveAndSendMessage(client)
        }
      } catch {
        case _: SocketException =>
          //remove client from list of all clients
          sendClientDisconnected(client.nick)
          Server.usersThreads -= client
      }
    }
  }

  //method that is sending to all clients list of available clients
  def printClientConnected(nick: String): Unit = {
    for {client <- clientsToSend(id)} {
      client.tcpServer.out.println(s"$nick connected")
    }
  }

  //method that is sending to client list of already connected clients
  def printAlreadyConnectedClients(): Unit = {
    val alreadyConnected = Server.usersThreads.map(_.nick)
    if (alreadyConnected.nonEmpty) out.println(s"Already connected are: ${alreadyConnected.mkString(", ")}")
  }

  //method that is parsing first message from client which contain all information about client
  def parseClient(msg: String): ConnectedClient = {
    val info = msg.split(" ")
    val clientId = UUID.fromString(info(0))
    val nick = info(1)
    val clientUdpPort = info(2).toInt

    ConnectedClient(this, nick, clientId, socket.getInetAddress, clientUdpPort)
  }

  //method that is sending to all clients that this client disconnected
  def sendClientDisconnected(nick: String): Unit = {
    for {client <- clientsToSend(id)} {
      println(s"$nick disconnected")
      client.tcpServer.out.println(s"$nick disconnected")
    }
  }

  //method that receives messages from client and sends it to all other clients
  def receiveAndSendMessage(connectedClient: ConnectedClient): Unit = {
    //receive message
    val msg = in.readLine
    println(s"Received message by TCP from ${connectedClient.clientId}-${connectedClient.nick}: $msg")
    for {client <- clientsToSend(id)} {
      //message has prefix "M " because it's text message
      client.tcpServer.out.println(s"M ${connectedClient.nick} is writing: $msg")
    }
  }
}
