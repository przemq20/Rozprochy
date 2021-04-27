package Utils

import akka.actor.typed.ActorRef

case class DispatcherRequest(
  station:          ActorRef[Command],
  queryId:          Int,
  firstSatelliteId: Int,
  range:            Int,
  timeout:          Int
) extends Command
