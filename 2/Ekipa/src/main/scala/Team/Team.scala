package Team

import com.rabbitmq.client._
import Utils.Config.{ generateId, EXCHANGE_NAME, HOSTNAME, RoutingKey }
import Utils.Order

import scala.Console.{ GREEN, RESET }

class Team(val teamName: String) {
  val teamId: String = generateId

  //create connection and channel
  val factory = new ConnectionFactory
  factory.setHost(HOSTNAME)
  val connection:     Connection = factory.newConnection
  val publishChannel: Channel    = connection.createChannel
  publishChannel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC)
  publishChannel.basicQos(1)

  scribe.info(s"Team id: $teamId")
  val processMessages = new ProcessTeamMessages(this, connection)

  def createOrder(equipment: String): Unit = {
    val order   = new Order(equipment, teamName, teamId)
    val message = Order.orderToJson(order).toString
    println(s"$RESET${GREEN}You are sending order for $equipment$RESET")
    publishChannel.basicPublish(EXCHANGE_NAME, s"${RoutingKey.allSuppliers}.$equipment", null, message.getBytes)
  }
}
