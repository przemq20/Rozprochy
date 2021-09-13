package server

import Office.{ Account, CallbackReceiverPrx, CallbackSender, Task }
import com.zeroc.Ice.{ CommunicatorDestroyedException, Current }

import java.util.concurrent.{ Executors, ScheduledExecutorService, TimeUnit }

class CallbackSenderI extends CallbackSender {
  private val _executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor

  def destroy(): Unit = {
    System.out.println("destroying callback sender")
    _executorService.shutdown()
    try _executorService.awaitTermination(10, TimeUnit.SECONDS)
    catch {
      case _: InterruptedException => // ignored
    }
  }

  def start(callbackReceiverPrx: CallbackReceiverPrx, task: Task): Unit = {
    invokeCallback(callbackReceiverPrx: CallbackReceiverPrx, task: Task)
  }

  private def invokeCallback(callbackReceiverPrx: CallbackReceiverPrx, task: Task): Unit = {
    try callbackReceiverPrx.callbackAsync(task)
    catch {
      case _: CommunicatorDestroyedException =>
    }
  }

  override def disconnect(account: Account, current: Current): Unit = {
    Server.clients.remove(account)
  }
}
