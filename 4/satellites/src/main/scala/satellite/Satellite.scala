package satellite

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ AbstractBehavior, ActorContext, Behaviors }
import Utils._

class Satellite(context: ActorContext[Command], id: Int) extends AbstractBehavior[Command](context) {

  override def onMessage(msg: Command): Behavior[Command] = {
    msg match {
      case request: SatelliteRequest =>
        try {
          val startTime  = System.currentTimeMillis
          val status     = SatelliteAPI.getStatus(id)
          val endTime    = System.currentTimeMillis - startTime
          val successful = if (endTime < request.timeout) true else false
          //send response to DispatcherConnector
          request.dispatcher ! SatelliteResponse(request.queryId, successful, status, id, request.station)
          Behaviors.stopped
        } catch {
          case _: Exception =>
            scribe.info(s"Error in satellite ${request.satelliteId}")
            request.dispatcher ! SatelliteResponse(
              request.queryId,
              successful = false,
              Status.BATTERY_LOW,
              id,
              request.station
            )
            Behaviors.stopped
        }
    }
  }
}

object Satellite {
  def apply(id: Int): Behavior[Command] = Behaviors.setup(context => new Satellite(context, id))
}
