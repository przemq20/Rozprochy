package server

import Office._
import com.zeroc.Ice.Current

import java.util.UUID
import scala.collection.mutable.ListBuffer
import scala.util.Random

class ClientAccountManagerI extends ClientAccountManager {
  var lastId: Long = 0

  override def createTask(account: Account, taskType: TaskType, current: Current): Task = {
    val id   = Math.abs(UUID.randomUUID().getMostSignificantBits)
    val time = Math.abs(Random.nextInt(taskType.value() * 30 + 30) + taskType.value() * 30)
    val task = new Task(taskType, id, time, Status.RECEIVED)
    Server.tasks.getOrElse(account, ListBuffer.empty).addOne(task)
    scribe.info(s"Task $taskType id: $id successfully created")
    task
  }

  override def getStatus(account: Account, current: Current): Array[Task] = {
    Server.tasks(account).toArray
  }
}
