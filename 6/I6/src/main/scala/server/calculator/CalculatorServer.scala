package server.calculator

import io.grpc.{ Server, ServerBuilder }

import java.util.concurrent.Executors

class CalculatorServer(port: Int) {
  val server: Server = ServerBuilder
    .forPort(port)
    .executor(Executors.newFixedThreadPool(16))
    .addService(new CalculatorImpl)
    .build()

  Runtime.getRuntime.addShutdownHook(new Thread(() => CalculatorServer.this.stop()))

  def start(): Unit = {
    this.server.start
  }

  private def stop(): Unit = {
    if (this.server != null) this.server.shutdown
  }

  def waitForShutdown(): Unit = {
    if (server != null) server.awaitTermination()
  }
}
