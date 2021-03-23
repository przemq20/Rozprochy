package Supplier

import com.rabbitmq.client._
import Utils.Config._

import scala.collection.mutable.ArrayBuffer

class Supplier(val buffer: ArrayBuffer[String], val supplierName: String) {
  val supplierId: String = generateId

  //create connection
  private val factory = new ConnectionFactory
  factory.setHost(HOSTNAME)
  private val connection: Connection = factory.newConnection

  scribe.info(s"Supplier id: $supplierId")

  val processSupplierMessages = new ProcessSupplierMessages(this, connection)
}
