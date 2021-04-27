package dispatcher

import akka.actor.typed.{ Behavior, DispatcherSelector, SupervisorStrategy }
import Utils._
import akka.actor.typed.scaladsl.{ AbstractBehavior, ActorContext, Behaviors }

import java.util.UUID

class Dispatcher(context: ActorContext[Command]) extends AbstractBehavior[Command](context) {

  override def onMessage(msg: Command): Behavior[Command] = {
    msg match {
      case request: DispatcherRequest => onRequest(request)
    }
  }

  def onRequest(request: DispatcherRequest): Behavior[Command] = {
    //spawn DispatcherConnector - one for each request
    context
      .spawn(
        Behaviors.supervise(DispatcherConnector.apply()).onFailure(SupervisorStrategy.restart),
        "SatelliteActor" + UUID.randomUUID(),
        DispatcherSelector.fromConfig("my-dispatcher")
      ) ! request
    this
  }
}

object Dispatcher {
  def apply(): Behavior[Command] = {
    scribe.info("Starting Dispatcher")
    Behaviors.setup(context => new Dispatcher(context))
  }
}
