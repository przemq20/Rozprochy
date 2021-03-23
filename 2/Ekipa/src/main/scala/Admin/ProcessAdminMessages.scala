package Admin

import com.rabbitmq.client._
import spray.json.JsonParser
import Utils.Config.{ EXCHANGE_NAME, RoutingKey }
import Utils.Order

import java.nio.charset.StandardCharsets
import scala.Console.{ BOLD, CYAN, MAGENTA, RESET }

class ProcessAdminMessages(connection: Connection) {
  readMessages()

  private def readMessages(): Unit = {
    val queueName = "ADMIN"

    val channel: Channel = connection.createChannel
    channel.queueDeclare(queueName, false, false, true, null)
    channel.queueBind(queueName, EXCHANGE_NAME, "#")
    val consumer: Consumer = createInfoConsumer(channel)
    channel.basicConsume(queueName, false, consumer)
    channel.basicQos(1)
  }

  private def createInfoConsumer(channel: Channel): Consumer = new DefaultConsumer(channel) {
    override def handleDelivery(
      consumerTag: String,
      envelope:    Envelope,
      properties:  AMQP.BasicProperties,
      body:        Array[Byte]
    ): Unit = {
      val message = new String(body, StandardCharsets.UTF_8)
      channel.basicAck(envelope.getDeliveryTag, false)
      if (!List(RoutingKey.allTeams, RoutingKey.allSuppliers, RoutingKey.all).contains(envelope.getRoutingKey)) {
        val order = Order.jsonToOrder(JsonParser(message))
        if (order.done) {
          println(
            s"$RESET$BOLD${CYAN}Supplier ${order.supplierName.getOrElse("Unknown")}-${order.supplierId
              .getOrElse("Unknown")} handled order ${order.orderId
              .getOrElse("Unknown")} (${order.equipment}) for ${order.teamName}$RESET"
          )
        } else {
          println(s"$RESET$BOLD${MAGENTA}Team ${order.teamName}-${order.teamId} is requesting ${order.equipment}$RESET")
        }
      }
    }
  }

}
