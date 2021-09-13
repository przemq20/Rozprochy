package client

import Office._
import com.zeroc.Ice

import java.util.concurrent.Executors
import scala.concurrent.{ ExecutionContext, ExecutionContextExecutorService, Future }
import scala.io.StdIn.readLine

object Client extends App {
  implicit val context: ExecutionContextExecutorService =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(100))

  val iceProperties = Ice.Util.createProperties
  initProperties(iceProperties)
  val initData = new Ice.InitializationData
  initData.properties = iceProperties

  var communicator = com.zeroc.Ice.Util.initialize(initData)
  communicator.getProperties.setProperty("Ice.Default.Package", "com.zeroc.demos.Ice.bidir")

  var officeBase         = communicator.stringToProxy("Office:default -p 10000")
  var accountBase        = communicator.stringToProxy("ClientAccount:default -p 10000")
  var taskProcessorBase  = communicator.stringToProxy("TaskProcessor:default -p 10000")
  var callbackSenderBase = communicator.stringToProxy("CallbackSender:default -p 10000")

  var officePrx        = AccountsManagerPrx.checkedCast(officeBase)
  var clientAccountPrx = ClientAccountManagerPrx.checkedCast(accountBase)
  var taskProcessorPrx = TaskProcessorPrx.checkedCast(taskProcessorBase)

  var server = CallbackSenderPrx.checkedCast(callbackSenderBase)

  var adapter = communicator.createObjectAdapter("")
  var proxy   = CallbackReceiverPrx.uncheckedCast(adapter.addWithUUID(new CallbackReceiverI(taskProcessorPrx)))
  adapter.activate()
  server.ice_getConnection.setAdapter(adapter)

  var account: Option[Account] = None
  var name:    Option[String]  = None
  init()

  def init(): Unit = {
    communicator       = com.zeroc.Ice.Util.initialize(args)
    officeBase         = communicator.stringToProxy("Office:default -p 10000")
    accountBase        = communicator.stringToProxy("ClientAccount:default -p 10000")
    taskProcessorBase  = communicator.stringToProxy("TaskProcessor:default -p 10000")
    callbackSenderBase = communicator.stringToProxy("CallbackSender:default -p 10000")

    officePrx        = AccountsManagerPrx.checkedCast(officeBase)
    clientAccountPrx = ClientAccountManagerPrx.checkedCast(accountBase)
    taskProcessorPrx = TaskProcessorPrx.checkedCast(taskProcessorBase)
    server           = CallbackSenderPrx.checkedCast(callbackSenderBase)

    adapter = communicator.createObjectAdapter("")
    proxy   = CallbackReceiverPrx.uncheckedCast(adapter.addWithUUID(new CallbackReceiverI(taskProcessorPrx)))
    adapter.activate()
    server.ice_getConnection.setAdapter(adapter)

    account = None
    name    = None

    login()

    chooseAction()
  }

  def chooseAction(): Unit = {
    var line = ""
    while (line != "3") {
      println("""Choose Action:
                |1 - check status of your tasks
                |2 - create new task
                |3 - logout
                |""".stripMargin)
      line = readLine()
      line match {
        case "1" =>
          val tasks = clientAccountPrx.getStatus(account.get)
          if (tasks.isEmpty) println(Console.RED + "You have not created any tasks yet" + Console.RESET)
          else {
            tasks.foreach { task =>
              if (task.status == Status.DONE)
                println(Console.CYAN + s"Task: ${task.taskType} id: ${task.id} status: ${task.status}" + Console.RESET)
              else if (task.status == Status.RECEIVED)
                println(Console.BLUE + s"Task: ${task.taskType} id: ${task.id} status: ${task.status}" + Console.RESET)
              else println(Console.GREEN + s"Task: ${task.taskType} id: ${task.id} status: ${task.status}" + Console.RESET)
            }
          }
        case "2" =>
          println("""What kind of task do you want to create?
                    |1 - Receive new ID Card
                    |2 - Register Car
                    |3 - Send Tax form
                    |4 - quit
                    |""".stripMargin)
          val taskType = readLine
          taskType match {
            case "1" | "2" | "3" =>
              val createdTask = clientAccountPrx.createTask(account.get, TaskType.valueOf(Integer.valueOf(taskType) - 1))
              Future(taskProcessorPrx.processTask(createdTask, account.get))

              println(
                Console.CYAN +
                  s"Created task ${createdTask.taskType}, id: ${createdTask.id}. Estimated time: ${createdTask.getTime}"
                  + Console.RESET
              )
            case _ =>
          }
        case "3" => server.disconnect(account.get)
        case _   =>
      }
    }
    logout()
  }

  def login(): Unit = {
    while (account.isEmpty) {
      println("""Welcome to the office! Do you want to:
                |1 - register new account
                |2 - login to an existing account
                |3 - exit application
                |""".stripMargin)

      readLine match {
        case "1" =>
          println("REGISTER FORM:")
          println("Enter name:")
          name = Some(readLine)
          if (name.getOrElse("").nonEmpty) {
            try {
              account = Some(officePrx.register(name.get, proxy))
              scribe.info("Account " + name.get + " id: " + account.get.id + " successfully registered.")
            } catch {
              case _: UserAlreadyExists =>
                scribe.warn("Client Already Registered, please login")
            }
          }

        case "2" =>
          println("Login FORM:")
          println("Enter name:")
          name = Some(readLine)
          if (name.getOrElse("").nonEmpty) {
            try {
              account = Some(officePrx.login(name.get, proxy))
            } catch {
              case _: UserNotExists =>
                scribe.warn("Client not exists in database, please register")
            }
            if (account.isDefined) {
              println("Logged as " + account.get.name + " id: " + account.get.id)
            }
          }
        case "3" =>
          System.exit(0)
        case _ =>
      }
    }
  }

  def logout(): Unit = {
    server.disconnect(account.get)
    communicator.close()
    account = None
    name    = None
    init()
  }

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
