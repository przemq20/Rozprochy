package satellite

import scala.util.Random

object SatelliteAPI {
  def getStatus(satelliteIndex: Int): Status = {
    Thread.sleep(100 + Random.nextInt(400))
    val p = Random.nextDouble()
    p match {
      case res if res < 0.8  => Status.OK
      case res if res < 0.9  => Status.BATTERY_LOW
      case res if res < 0.95 => Status.NAVIGATION_ERROR
      case _                 => Status.PROPULSION_ERROR
    }
  }
}
