package Admin

import com.rabbitmq.client._
import Utils.Config.{ EXCHANGE_NAME, HOSTNAME, RoutingKey }

class Admin {
  //create connection and channel
  val factory = new ConnectionFactory
  factory.setHost(HOSTNAME)
  val connection:     Connection = factory.newConnection
  val publishChannel: Channel    = connection.createChannel
  publishChannel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC)
  publishChannel.basicQos(1)

  val processMessages = new ProcessAdminMessages(connection)

  def sendToAllTeams(message: String): Unit = {
    scribe.info(s"""Sending message: "$message" to all teams""")
    publishChannel.basicPublish(EXCHANGE_NAME, RoutingKey.allTeams, null, message.getBytes)
  }

  def sendToAllSuppliers(message: String): Unit = {
    scribe.info(s"""Sending message: "$message" to all suppliers""")
    publishChannel.basicPublish(EXCHANGE_NAME, RoutingKey.allSuppliers, null, message.getBytes)
  }

  def sendToAll(message: String): Unit = {
    scribe.info(s"""Sending message: "$message" to all teams and suppliers""")
    publishChannel.basicPublish(EXCHANGE_NAME, RoutingKey.all, null, message.getBytes)
  }
}
