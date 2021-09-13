import logging
import random
import sys
import time

import Ice
import coloredlogs

import Office

accounts = []
tasks = {}
clients = {}


class AccountsManagerI(Office.AccountsManager):
    def register(self, name, callbackReceiverPrx: Office.CallbackReceiverPrx, current=None):
        if name in map(a, accounts):
            logging.warning("Client Already Registered, please login")
            raise Office.UserAlreadyExists
        else:
            id = random.randint(1000, 1000000000)
            client = Office.Account(id, name)
            logging.debug("New Account registered: " + client.name + " id: " + str(client.id))
            accounts.append(client)
            tasks.update({client: []})
            clients.update({client: callbackReceiverPrx.ice_fixed(current.con)})
            return client

    def login(self, name, callbackReceiverPrx: Office.CallbackReceiverPrx, current=None):
        if name in map(a, accounts):
            account = accounts[list(map(a, accounts)).index(name)]
            logging.debug(f"Client {name} logged into system")
            if account in clients.keys():
                clients.pop(account)
            clients.update({account: callbackReceiverPrx.ice_fixed(current.con)})
            self.sendProcessed(account, callbackReceiverPrx.ice_fixed(current.con))
            return account
        else:
            logging.warning("Account is not register in database")
            raise Office.UserNotExists

    def sendProcessed(self, account, receiver):
        listOfProcessed = []
        if account in tasks.keys():
            listOfProcessed = list(filter(b, tasks[account]))
        for task in listOfProcessed:
            callbackSenderI.start(receiver, task)


def a(account: Office.Account):
    return account.name


def b(task: Office.Task):
    return task.status == Office.Status.PROCESSED


class ClientAccountManagerI(Office.ClientAccountManager):
    def createTask(self, account, taskType, current=None):
        id = random.randint(1000, 1000000000)
        time = abs(random.randint(0, taskType.value * 30 + 30)) + taskType.value * 30
        task = Office.Task(taskType, id, time, Office.Status.RECEIVED)
        copy = list(tasks[account])
        copy.append(task)
        tasks.pop(account)
        tasks.update({account: copy})
        logging.debug(f"Task {task.taskType} id: {task.id} successfully created")
        return task

    def getStatus(self, account: Office.Account, current=None):
        if account in tasks.keys():
            return list(tasks[account])
        else:
            return []


class TaskProcessorI(Office.TaskProcessor):
    def processTask(self, task: Office.Task, account, current=None):
        time.sleep(task.time)
        tt = filter(lambda seq: c(seq, task), tasks[account])
        for ttt in tt:
            copy = list(tasks[account])
            index = copy.index(ttt)
            copy.pop(index)
            copy.append(Office.Task(task.taskType, task.id, task.time, Office.Status.PROCESSED))
            tasks.pop(account)
            tasks.update({account: copy})
        callbackSenderI.start(clients.get(account),
                              Office.Task(task.taskType, task.id, task.time, Office.Status.PROCESSED))

        logging.debug(f"Processing task {task.taskType} id: {task.id} is done")

    def sendDeliveryReceipt(self, deliveryReceipt, current=None):
        id = deliveryReceipt.taskId
        account = deliveryReceipt.account
        logging.debug(f"Received delivery Receipt for task id: {id}")
        taskList = list(filter(lambda seq: d(seq, id), tasks[account]))
        for task in taskList:
            position = list(tasks[account]).index(task)
            copy = list(tasks[account])
            copy.pop(position)
            copy.append(Office.Task(task.taskType, task.id, task.time, Office.Status.DONE))
            tasks.pop(account)
            tasks.update({account: copy})


def c(task: Office.Task, task2: Office.Task):
    return task.id == task2.id


def d(task: Office.Task, id):
    return task.id == id


class CallbackSenderI(Office.CallbackSender):
    def start(self, callbackReceiverPrx, task):
        if callbackReceiverPrx is not None:
            callbackReceiverPrx.callbackAsync(task)

    def disconnect(self, account, current=None):
        if account in clients:
            clients.pop(account)


callbackSenderI = CallbackSenderI()


def main():
    logging.root.setLevel(logging.DEBUG)
    coloredlogs.install(level='DEBUG')
    logging.info("Starting Server")

    props = Ice.createProperties(sys.argv)
    props.setProperty("Ice.Override.ConnectTimeout", "70")
    props.setProperty("Ice.ThreadPool.Client.Size", "100")
    props.setProperty("Ice.ThreadPool.Client.SizeMax", "1000")
    props.setProperty("Ice.ThreadPool.Client.StackSize", "131072")
    props.setProperty("Ice.ThreadPool.Server.SizeMax", "1000")
    props.setProperty("Ice.ThreadPool.Server.StackSize", "131072")
    props.setProperty("Ice.MessageSizeMax", "102400")
    id = Ice.InitializationData()
    id.properties = props
    logging.info("Initializing properties")

    with Ice.initialize(id) as communicator:
        adapter = communicator.createObjectAdapterWithEndpoints("Office", "default -p 10000")
        accountsManagerI = AccountsManagerI()
        clientAccountI = ClientAccountManagerI()
        taskProcessorI = TaskProcessorI()

        adapter.add(accountsManagerI, communicator.stringToIdentity("Office"))
        adapter.add(clientAccountI, communicator.stringToIdentity("ClientAccount"))
        adapter.add(taskProcessorI, communicator.stringToIdentity("TaskProcessor"))
        adapter.add(callbackSenderI, communicator.stringToIdentity("CallbackSender"))

        adapter.activate()
        logging.info("Server started")
        communicator.waitForShutdown()


if __name__ == "__main__":
    main()
