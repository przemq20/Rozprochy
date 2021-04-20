import org.apache.zookeeper.{ WatchedEvent, Watcher, ZooKeeper }
import org.apache.zookeeper.Watcher.Event

import java.io.IOException
import java.util.concurrent.CountDownLatch
import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn.readLine

object Main extends App {
  //if app has no provided arguments it will ask for application to run when zNode detected
  val exec = if (args.length == 0) {
    println("Enter command to run when /z zNode created:")
    readLine
  } else args(0)

  var process: Option[Process] = None
  val watchedZnode    = "/z"
  val connectionLatch = new CountDownLatch(1)
  val tree            = new ArrayBuffer[String]()
  val zooKeeper       = connect("127.0.0.1:2181")
  scribe.info("Zookeper starting")

  //createWatchers
  val childrenWatcher = createChildrenWatcher
  val watcher         = createWatcher

  zooKeeper.exists(watchedZnode, watcher)
  try zooKeeper.getChildren(watchedZnode, childrenWatcher)
  catch {
    case _: Exception =>
  }

  while (true) {
    val input = readLine
    if (input == "q") {
      zooKeeper.close()
      System.exit(0)
    } else if (input == "ls") printTree(watchedZnode)
  }

  def connect(host: String): ZooKeeper = {
    val zoo = new ZooKeeper(host, 200, new Watcher {
      override def process(event: WatchedEvent): Unit = {
        if (event.getState == Event.KeeperState.SyncConnected) connectionLatch.countDown()
      }
    })
    connectionLatch.await()
    zoo
  }

  def createChildrenWatcher: Watcher = {
    new Watcher {
      override def process(event: WatchedEvent): Unit = {
        if (event.getType == Event.EventType.NodeChildrenChanged) {
          try scribe.info(watchedZnode + " children count: " + zooKeeper.getAllChildrenNumber(watchedZnode))
          catch {
            case _: Exception =>
          }
        }
        try zooKeeper.getChildren(watchedZnode, this)
        catch {
          case _: Exception =>
        }
      }
    }
  }

  def createWatcher: Watcher = {
    new Watcher {
      override def process(event: WatchedEvent): Unit = {
        if (event.getType == Event.EventType.NodeCreated) {
          openApp()
          try {
            zooKeeper.getChildren(watchedZnode, childrenWatcher)
          }
          catch {
            case _: Exception =>
          }
        } else if (event.getType == Event.EventType.NodeDeleted) {
          try closeApp()
          catch {
            case _: IOException =>
          }
        }
        try zooKeeper.exists(watchedZnode, this)
        catch {
          case _: Exception =>
        }
      }
    }
  }

  def openApp(): Unit = {
    scribe.info(s"Received command open $exec")
    val pb: ProcessBuilder = new ProcessBuilder(exec)
    process = Option(pb.start())
  }A

  private def closeApp(): Unit = {
    scribe.warn(s"Received command close $exec")
    if (process.isDefined) process.get.destroy()
    process = None
  }

  def printTree(znode: String): Unit = {
    scribe.info("Received command printTree")
    displayTree(znode)
    println(tree.mkString("\n"))
    tree.clear()
  }

  private def displayTree(znode: String): Unit = {
    val childList = zooKeeper.getChildren(znode, false)
    tree.addOne(znode)
    childList.forEach { child =>
      displayTree(znode + "/" + child)
    }
  }
}
