package server.stream

import io.grpc.{ Server, ServerBuilder }

import java.util.concurrent.Executors

class StreamTesterServer(port: Int) {
  val server: Server = ServerBuilder
    .forPort(port)
    .executor(Executors.newFixedThreadPool(16))
    .addService(new StreamTesterImpl)
    .build()

  Runtime.getRuntime.addShutdownHook(new Thread(() => StreamTesterServer.this.stop()))

  def start(): Unit = {
    server.start
  }

  private def stop(): Unit = {
    if (server != null) server.shutdown
  }

  def waitForShutdown(): Unit = {
    if (server != null) server.awaitTermination()
  }
}
