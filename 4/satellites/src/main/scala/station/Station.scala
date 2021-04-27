package station

import akka.actor.typed.scaladsl.{ AbstractBehavior, ActorContext, Behaviors }
import Utils.{ Command, DispatcherRequest, DispatcherResponse, StationRequest }
import akka.actor.typed.Behavior

import java.util.UUID
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class Station(context: ActorContext[Command], name: String) extends AbstractBehavior[Command](context) {
  val usedIds:    ArrayBuffer[Int]       = ArrayBuffer[Int]()
  val startTimes: mutable.Map[Int, Long] = mutable.Map[Int, Long]()
  override def onMessage(msg: Command): Behavior[Command] = {
    msg match {
      case response: DispatcherResponse => onResponse(response)
      case request:  StationRequest     => createRequest(request)
    }
  }

  def createRequest(begin: StationRequest): Behavior[Command] = {
    val queryId: Int = {
      var id: Int = 0
      do {
        id = Math.abs((UUID.randomUUID().getLeastSignificantBits % 100000).toInt)
      } while (usedIds.contains(id))
      id
    }
    val request = DispatcherRequest(
      context.self,
      queryId,
      begin.firstSatelliteId.getOrElse(100 + Random.nextInt(50)),
      begin.range.getOrElse(50),
      begin.timeout.getOrElse(300)
    )
    startTimes.addOne(queryId, System.currentTimeMillis)
    //satellites has ids from 100 and 199
    if (request.firstSatelliteId + request.range > 200) begin.dispatcher ! request.copy(range = 200 - request.firstSatelliteId)
    else begin.dispatcher ! request
    this
  }

  def onResponse(response: DispatcherResponse): Behavior[Command] = {
    scribe.info(s"Station ${this.context.self} received results of query: ${response.queryId}")
    val totalTime: Long = System.currentTimeMillis() - startTimes.remove(response.queryId).getOrElse(0L)
    println(s"""
               | Station: $name
               | QueryId: ${response.queryId}
               | Succesful: ${response.successful}%
               | Total time: $totalTime
               | Map size: ${response.errors.size}:
               |   ${response.errors
                 .map(pair => pair._1 + " => " + pair._2)
                 .toSeq
                 .sortBy(_.slice(0, 2))
                 .mkString("\n   ")}
      """.stripMargin)
    this
  }

}

object Station {
  def apply(name: String): Behavior[Command] = Behaviors.setup(context => new Station(context, name))
}
