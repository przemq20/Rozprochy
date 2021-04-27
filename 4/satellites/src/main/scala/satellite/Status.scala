package satellite

sealed trait Status

object Status {
  case object OK extends Status
  case object BATTERY_LOW extends Status
  case object PROPULSION_ERROR extends Status
  case object NAVIGATION_ERROR extends Status
}
