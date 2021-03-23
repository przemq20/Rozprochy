package Supplier

import com.rabbitmq.client._
import spray.json.JsonParser
import Utils.Config.{ EXCHANGE_NAME, RoutingKey }
import Utils.Order

import java.nio.charset.StandardCharsets
import scala.Console.{ BOLD, CYAN, MAGENTA, RESET }

class ProcessSupplierMessages(supplier: Supplier, connection: Connection) {
  readAdminMessages()
  readTeamMessages()

  private def readAdminMessages(): Unit = {
    val queueName = s"taskManager.supplier.${supplier.supplierId}"

    val channel: Channel = connection.createChannel
    channel.queueDeclare(queueName, false, false, true, null)
    channel.queueBind(queueName, EXCHANGE_NAME, RoutingKey.allSuppliers)
    channel.queueBind(queueName, EXCHANGE_NAME, RoutingKey.all)
    val consumer: Consumer = adminConsumer(channel)
    channel.basicConsume(queueName, false, consumer)
    channel.basicQos(1)
  }

  private def readTeamMessages(): Unit = {
    val channel: Channel = connection.createChannel
    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC)
    channel.basicQos(1)

    supplier.buffer.foreach { equipment =>
      channel.queueDeclare(equipment, false, false, false, null)
      channel.queueBind(equipment, EXCHANGE_NAME, s"${RoutingKey.allSuppliers}.$equipment")
      channel.basicConsume(equipment, false, orderConsumer(channel))
    }
  }

  private def adminConsumer(channel: Channel): Consumer = new DefaultConsumer(channel) {
    override def handleDelivery(
      consumerTag: String,
      envelope:    Envelope,
      properties:  AMQP.BasicProperties,
      body:        Array[Byte]
    ): Unit = {
      val message = new String(body, StandardCharsets.UTF_8)
      channel.basicAck(envelope.getDeliveryTag, false)
      println(s"$RESET$BOLD${MAGENTA}Received message from admin: $message$RESET")
    }
  }

  private def orderConsumer(channel: Channel): Consumer = {
    new DefaultConsumer(channel) {
      override def handleDelivery(
        consumerTag: String,
        envelope:    Envelope,
        properties:  AMQP.BasicProperties,
        body:        Array[Byte]
      ): Unit = {
        val message        = new String(body, StandardCharsets.UTF_8)
        val receivedOrder  = Order.jsonToOrder(JsonParser(message))
        val processedOrder = receivedOrder.giveId.processOrder(supplier.supplierId, supplier.supplierName)

        println(
          s"$RESET$BOLD${CYAN}Received order for ${processedOrder.equipment} from team ${processedOrder.teamName}-id:${processedOrder.teamId}$RESET"
        )
        channel.basicAck(envelope.getDeliveryTag, false)
        scribe.info(
          s"Order ${processedOrder.orderId.getOrElse("Unknown")} is ready - sending confirmation to ${processedOrder.teamName}"
        )
        channel.basicPublish(
          EXCHANGE_NAME,
          s"${RoutingKey.allTeams}.${processedOrder.teamId}",
          null,
          Order.orderToJson(processedOrder).toString.getBytes
        )
      }
    }
  }
}
