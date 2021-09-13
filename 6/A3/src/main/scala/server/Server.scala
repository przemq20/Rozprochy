package server

import Office.{ Account, CallbackReceiverPrx, Task }
import com.zeroc.Ice

import scala.collection.{ concurrent, mutable }

object Server extends App {
  val accounts = new mutable.ListBuffer[Account]
  val tasks:   concurrent.Map[Account, mutable.ListBuffer[Task]] = concurrent.TrieMap.empty
  val clients: concurrent.Map[Account, CallbackReceiverPrx]      = concurrent.TrieMap.empty

  scribe.info("Starting Server")
  val clientAccountI  = new ClientAccountManagerI
  val taskProcessorI  = new TaskProcessorI
  val callbackSenderI = new CallbackSenderI
  val officeI         = new AccountsManagerI

  val iceProperties = Ice.Util.createProperties
  initProperties(iceProperties)
  val initData = new Ice.InitializationData
  initData.properties = iceProperties
  scribe.info("Initializing properties")
  val communicator = com.zeroc.Ice.Util.initialize(initData)
  try {
    val adapter = communicator.createObjectAdapterWithEndpoints("Office", "default -p 10000")
    adapter.add(officeI, com.zeroc.Ice.Util.stringToIdentity("Office"))
    adapter.add(clientAccountI, com.zeroc.Ice.Util.stringToIdentity("ClientAccount"))
    adapter.add(taskProcessorI, com.zeroc.Ice.Util.stringToIdentity("TaskProcessor"))
    adapter.add(callbackSenderI, com.zeroc.Ice.Util.stringToIdentity("CallbackSender"))

    Runtime.getRuntime.addShutdownHook(new Thread(() => callbackSenderI.destroy()))

    adapter.activate()
    scribe.info("Server started")
    communicator.waitForShutdown()
  } finally if (communicator != null) communicator.close()

  def initProperties(iceProperties: Ice.Properties): Unit = {
    iceProperties.setProperty("Ice.Override.ConnectTimeout", "70")
    iceProperties.setProperty("Ice.ThreadPool.Client.Size", "100")
    iceProperties.setProperty("Ice.ThreadPool.Client.SizeMax", "1000")
    iceProperties.setProperty("Ice.ThreadPool.Client.StackSize", "131072") //128k

    iceProperties.setProperty("Ice.ThreadPool.Server.SizeMax", "1000")
    iceProperties.setProperty("Ice.ThreadPool.Server.StackSize", "131072")
    iceProperties.setProperty("Ice.MessageSizeMax", "102400")
  }
}
