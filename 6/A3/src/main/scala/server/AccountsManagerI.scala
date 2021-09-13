package server

import Office._
import com.zeroc.Ice.Current

import java.util.UUID
import scala.collection.mutable.ListBuffer

class AccountsManagerI extends AccountsManager {
  override def register(name: String, callbackReceiverPrx: CallbackReceiverPrx, current: Current): Account = {
    if (Server.accounts.map(_.name).contains(name)) {
      scribe.info("Client Already Registered, please login")
      throw new UserAlreadyExists
    } else {
      val id:     Long    = Math.abs(UUID.randomUUID.getMostSignificantBits)
      val client: Account = new Account(id, name)

      scribe.info("New Client: " + client.name + " id: " + client.id)
      Server.accounts.addOne(client)
      Server.tasks.put(client, new ListBuffer[Task])
      Server.clients.addOne(client, callbackReceiverPrx.ice_fixed(current.con))
      scribe.info("Client Registered")
      client
    }
  }

  override def login(name: String, callbackReceiverPrx: CallbackReceiverPrx, current: Current): Account = {
    if (Server.accounts.map(_.name).contains(name)) {
      val account = Server.accounts(Server.accounts.map(_.name).indexOf(name))
      scribe.info(s"Client $name logged into system")
      if (Server.clients.contains(account)) Server.clients.remove(account)
      Server.clients.addOne(account, callbackReceiverPrx.ice_fixed(current.con))

      sendProcessed(account, callbackReceiverPrx.ice_fixed(current.con))
      account
    } else throw new UserNotExists
  }

  def sendProcessed(account: Account, receiver: CallbackReceiverPrx): Unit = {
    val processedTasks = Server.tasks.get(account).get.filter(_.status == Status.PROCESSED)
    processedTasks.foreach { task =>
      Server.callbackSenderI.start(receiver, task)
    }
  }
}
