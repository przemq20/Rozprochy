package Utils

import akka.actor.typed.ActorRef

case class SatelliteRequest(
  station:     ActorRef[Command],
  queryId:     Int,
  satelliteId: Int,
  timeout:     Int,
  dispatcher:  ActorRef[Command]
) extends Command
