package Utils

import java.util.UUID

object Config {
  val HOSTNAME      = "localhost"
  val EXCHANGE_NAME = "taskManager"

  //routing keys
  case object RoutingKey {
    val all          = "taskManager"
    val allTeams     = "taskManager.team"
    val allSuppliers = "taskManager.supplier"
  }

  def generateId: String = UUID.randomUUID.getLeastSignificantBits.toString.takeRight(8)
}
