package Utils

import spray.json._

case class Order(
  equipment:    String,
  teamName:     String,
  teamId:       String,
  orderId:      Option[String] = None,
  supplierId:   Option[String] = None,
  supplierName: Option[String] = None,
  done:         Boolean = false
) {
  def giveId: Order = copy(orderId = Some(Config.generateId))
  def processOrder(supplierId: String, supplierName: String): Order =
    copy(done = true, supplierId = Some(supplierId), supplierName = Some(supplierName))
}

object Order extends DefaultJsonProtocol {
  implicit val orderFormat: RootJsonFormat[Order] = jsonFormat7(Order.apply)

  def orderToJson(order: Order): JsValue = {
    order.toJson
  }

  def jsonToOrder(json: JsValue): Order = {
    json.convertTo[Order]
  }
}
