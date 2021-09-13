package server.stream

import io.grpc.stub.StreamObserver
import streamingProto.{ Request, StreamTesterGrpc, Number => GrpcNumber }

class StreamTesterImpl extends StreamTesterGrpc.StreamTesterImplBase {

  override def generatePrimeNumbers(request: Request, responseObserver: StreamObserver[GrpcNumber]): Unit = {
    scribe.info(s"Generating Prime Numbers from ${request.getLowerBound} to ${request.getUpperBound}")

    for (i <- request.getLowerBound to request.getUpperBound) {
      if (isPrime(i)) {
        val number = GrpcNumber.newBuilder.setValue(i).build
        Thread.sleep(500)
        responseObserver.onNext(number)
      }
    }
    responseObserver.onCompleted()
  }
  private def isPrime(n: Int): Boolean = (2 to math.sqrt(n).toInt).forall(x => n % x != 0)
}
