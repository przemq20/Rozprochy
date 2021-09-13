package server

import Office._
import com.zeroc.Ice.Current

import java.lang.Thread.sleep
import java.util.concurrent.Executors
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ ExecutionContext, ExecutionContextExecutorService, Future }

class TaskProcessorI extends TaskProcessor {
  implicit val context: ExecutionContextExecutorService =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(100))

  override def processTask(task: Task, account: Account, current: Current): Unit = {
    val future: Future[Unit] = Future {
      sleep(1000 * task.getTime)
      Server.tasks.getOrElse(account, ListBuffer.empty).filter(_.id == task.id).foreach { task =>
        val position = Server.tasks.getOrElse(account, ListBuffer.empty).indexOf(task)
        Server.tasks.getOrElse(account, ListBuffer.empty).remove(position)
        Server.tasks.getOrElse(account, ListBuffer.empty).addOne(new Task(task.taskType, task.id, Status.PROCESSED))
      }
      scribe.info(s"Processing task ${task.taskType} id: ${task.id} is done")
      Server.callbackSenderI.start(Server.clients(account), new Task(task.taskType, task.id, Status.PROCESSED))
    }
  }

  override def sendDeliveryReceipt(deliveryReceipt: DeliveryReceipt, current: Current): Unit = {
    val id      = deliveryReceipt.taskId
    val account = deliveryReceipt.account
    scribe.info(s"Received delivery Receipt for task id: $id")
    Server.tasks.getOrElse(account, ListBuffer.empty).filter(_.id == id).foreach { task =>
      val position = Server.tasks.getOrElse(account, ListBuffer.empty).indexOf(task)
      Server.tasks.getOrElse(account, ListBuffer.empty).remove(position)
      Server.tasks.getOrElse(account, ListBuffer.empty).addOne(new Task(task.taskType, task.id, Status.DONE))
    }
  }
}
