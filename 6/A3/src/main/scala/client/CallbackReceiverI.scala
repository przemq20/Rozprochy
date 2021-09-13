package client

import Office.{ CallbackReceiver, DeliveryReceipt, Task, TaskProcessorPrx }
import client.Client.context
import com.zeroc.Ice.Current

import scala.concurrent.Future

class CallbackReceiverI(taskProcessorPrx: TaskProcessorPrx) extends CallbackReceiver {
  override def callback(task: Task, current: Current): Unit = {
    Future {
      println(Console.RED + s"Task ${task.taskType}, id: ${task.id} is ready" + Console.RESET)
      while (Client.account.isEmpty) Thread.sleep(100)

      if (Client.account.isDefined)
        taskProcessorPrx.sendDeliveryReceipt(new DeliveryReceipt(Client.account.get, task.id))
    }
  }
}
