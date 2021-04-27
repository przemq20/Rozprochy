package Utils

import akka.actor.typed.ActorRef
import satellite.Status

case class SatelliteResponse(
  queryId:     Int,
  successful:  Boolean,
  status:      Status,
  satelliteId: Int,
  station:     ActorRef[Command]
) extends Command
