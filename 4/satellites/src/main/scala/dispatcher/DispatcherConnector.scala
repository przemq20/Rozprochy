package dispatcher

import akka.actor.typed.scaladsl.{ AbstractBehavior, ActorContext, Behaviors }
import Utils._
import akka.actor.typed.{ Behavior, DispatcherSelector, SupervisorStrategy }
import satellite.{ Satellite, Status }

import java.util.UUID
import scala.collection.{ concurrent, mutable }
import scala.collection.concurrent.TrieMap

class DispatcherConnector(context: ActorContext[Command]) extends AbstractBehavior[Command](context) {
  val queries: concurrent.Map[Int, Query] = TrieMap.empty

  override def onMessage(msg: Command): Behavior[Command] = {
    msg match {
      case request:         DispatcherRequest => onRequest(request)
      case stationResponse: SatelliteResponse => onResponse(stationResponse)
    }
  }

  def onRequest(request: DispatcherRequest): Behavior[Command] = {
    scribe.info(s"Received request: $request")
    queries.addOne((request.queryId, Query(request.queryId, request.range, mutable.Map[Int, Status](), 0, request.range)))
    //spawn satellites and send them request
    for (satelliteId <- request.firstSatelliteId until request.firstSatelliteId + request.range)
      context
        .spawn(
          Behaviors.supervise(Satellite.apply(satelliteId)).onFailure(SupervisorStrategy.restart),
          "SatelliteActor" + satelliteId + UUID.randomUUID(),
          DispatcherSelector.fromConfig("my-dispatcher")
        )
        .tell(SatelliteRequest(request.station, request.queryId, satelliteId, request.timeout, this.context.self))
    this
  }

  def onResponse(response: SatelliteResponse): Behavior[Command] = {
    val queryId = response.queryId
    queries(queryId).left = queries(queryId).left - 1
    if (response.successful) {
      queries(queryId).successful = queries(queryId).successful + 1
      response.status match {
        case Status.OK =>
        case Status.BATTERY_LOW | Status.NAVIGATION_ERROR | Status.PROPULSION_ERROR =>
          queries(queryId).errors.addOne(response.satelliteId, response.status)
      }
    }
    //if received all responses from satellites send response to the station
    if (queries(queryId).left == 0) {
      val dispatcherResponse = DispatcherResponse(
        queryId    = queryId,
        errors     = queries(queryId).errors.toMap,
        successful = queries(queryId).successful * 100 / queries(queryId).range
      )
      scribe.info(s"Sending response to station ${response.station} - query: $queryId")
      response.station ! dispatcherResponse
      queries -= response.queryId
      Behaviors.stopped
    }
    this
  }
}

object DispatcherConnector {
  def apply(): Behavior[Command] = {
    scribe.info("Starting Dispatcher")
    Behaviors.setup(context => new DispatcherConnector(context))
  }
}
