package Team

import com.rabbitmq.client._
import spray.json.JsonParser
import Utils.{ Config, Order }
import Utils.Config.{ EXCHANGE_NAME, RoutingKey }

import java.nio.charset.StandardCharsets
import scala.Console.{ BOLD, CYAN, MAGENTA, RESET }

class ProcessTeamMessages(team: Team, connection: Connection) {
  suppliersMessages()
  adminMessages()

  private def suppliersMessages(): Unit = {
    val queueName: String = s"taskManager.team.${team.teamId}"

    val channel: Channel = connection.createChannel
    channel.queueDeclare(queueName, false, false, true, null)
    channel.queueBind(queueName, EXCHANGE_NAME, s"${RoutingKey.allTeams}.${team.teamId}")

    val consumer: Consumer = createSuppliersConsumer(channel)
    channel.basicConsume(queueName, false, consumer)
    channel.basicQos(1)
  }

  private def adminMessages(): Unit = {
    val queueName: String = s"taskManager.team.${team.teamId}.admin"

    val channel: Channel = connection.createChannel
    channel.queueDeclare(queueName, false, false, true, null)
    channel.queueBind(queueName, EXCHANGE_NAME, RoutingKey.allTeams)
    channel.queueBind(queueName, EXCHANGE_NAME, RoutingKey.all)

    val consumer: Consumer = createAdminConsumer(channel)
    channel.basicConsume(queueName, false, consumer)
    channel.basicQos(1)
  }

  private def createSuppliersConsumer(channel: Channel): Consumer = new DefaultConsumer(channel) {
    override def handleDelivery(
      consumerTag: String,
      envelope:    Envelope,
      properties:  AMQP.BasicProperties,
      body:        Array[Byte]
    ): Unit = {
      val message       = JsonParser(new String(body, StandardCharsets.UTF_8))
      val receivedOrder = Order.jsonToOrder(message)
      channel.basicAck(envelope.getDeliveryTag, false)
      if (receivedOrder.done) {
        println(s"""$RESET$BOLD${CYAN}Order for ${receivedOrder.equipment} has been done by supplier ${receivedOrder.supplierId
                     .getOrElse(
                       "Unknown"
                     )} - ${receivedOrder.supplierName.getOrElse("Unknown")}$RESET""".stripMargin)
      } else {
        scribe.error(" For some reason your order is not done")
      }
    }
  }

  private def createAdminConsumer(channel: Channel): Consumer = new DefaultConsumer(channel) {
    override def handleDelivery(
      consumerTag: String,
      envelope:    Envelope,
      properties:  AMQP.BasicProperties,
      body:        Array[Byte]
    ): Unit = {
      println(s"$RESET$BOLD${MAGENTA}Received message from admin: ${new String(body, StandardCharsets.UTF_8)}")
      channel.basicAck(envelope.getDeliveryTag, false)
    }
  }
}
