import akka.actor.typed.{ ActorSystem, Behavior, DispatcherSelector, Terminated }
import akka.actor.typed.scaladsl.Behaviors
import com.typesafe.config.ConfigFactory
import dispatcher.Dispatcher
import station.Station
import Utils.StationRequest

import java.io.File
import scala.concurrent.ExecutionContextExecutor
import scala.util.Random

object Main extends App {
  scribe.info("Program starting")
  //load config
  val configFile = new File("src/main/resources/dispatcher.conf")
  val config     = ConfigFactory.parseFile(configFile)
  scribe.info(s"Confing: $config")
  ActorSystem.create(create(), "actorSystem", config)

  def create(): Behavior[Unit] = {
    Behaviors.setup { context =>
      implicit val executionContext: ExecutionContextExecutor =
        context.system.dispatchers.lookup(DispatcherSelector.fromConfig("my-dispatcher"))

      val dispatcher = context.spawn(Dispatcher(), "dispatcher", DispatcherSelector.fromConfig("my-dispatcher"))

      //spawn Stations
      val station1 = context
        .spawn(Station("Station1"), "Station1", DispatcherSelector.fromConfig("my-dispatcher"))
      val station2 = context
        .spawn(Station("Station2"), "Station2", DispatcherSelector.fromConfig("my-dispatcher"))
      val station3 = context
        .spawn(Station("Station3"), "Station3", DispatcherSelector.fromConfig("my-dispatcher"))

      //send requests to Stations
      station1 ! StationRequest(dispatcher, Some(100 + Random.nextInt(50)), Some(50), Some(300))
      station1 ! StationRequest(dispatcher, Some(100 + Random.nextInt(50)), Some(50), Some(300))
      station2 ! StationRequest(dispatcher, Some(100 + Random.nextInt(50)), Some(50), Some(300))
      station2 ! StationRequest(dispatcher, Some(100 + Random.nextInt(50)), Some(50), Some(300))
      station3 ! StationRequest(dispatcher, Some(100 + Random.nextInt(50)), Some(50), Some(300))
      station3 ! StationRequest(dispatcher, Some(100 + Random.nextInt(50)), Some(50), Some(300))

      Behaviors.receiveSignal {
        case (_, Terminated(_)) => Behaviors.stopped
      }
    }
  }
}
