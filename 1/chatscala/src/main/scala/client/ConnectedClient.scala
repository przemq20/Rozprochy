package client

import server.TcpServer

import java.net.InetAddress
import java.util.UUID

//Simple case class to identify clients
case class ConnectedClient(tcpServer: TcpServer, nick: String, clientId: UUID, inetAddress: InetAddress, udpPort: Int)
