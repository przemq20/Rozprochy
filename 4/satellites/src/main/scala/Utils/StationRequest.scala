package Utils

import akka.actor.typed.ActorRef

case class StationRequest(
  dispatcher:       ActorRef[Command],
  firstSatelliteId: Option[Int] = None,
  range:            Option[Int] = None,
  timeout:          Option[Int] = None
) extends Command
