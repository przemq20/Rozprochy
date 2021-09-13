package server.calculator

import arithmetic.{ ArithmeticOpArguments, ArithmeticOpResult }
import calculator.CalculatorGrpc
import io.grpc.stub.StreamObserver

class CalculatorImpl extends CalculatorGrpc.CalculatorImplBase {
  override def add(request: ArithmeticOpArguments, responseObserver: StreamObserver[ArithmeticOpResult]): Unit = {
    scribe.info("addRequest (" + request.getArg1 + ", " + request.getArg2 + ")")
    val value  = request.getArg1 + request.getArg2
    val result = ArithmeticOpResult.newBuilder.setRes(value).build
    if (request.getArg1 > 100 && request.getArg2 > 100)
      Thread.sleep(5000)
    responseObserver.onNext(result)
    responseObserver.onCompleted()
  }

  override def subtract(request: ArithmeticOpArguments, responseObserver: StreamObserver[ArithmeticOpResult]): Unit = {
    scribe.info("subtractRequest (" + request.getArg1 + ", " + request.getArg2 + ")")
    val value  = request.getArg1 - request.getArg2
    val result = ArithmeticOpResult.newBuilder.setRes(value).build
    responseObserver.onNext(result)
    responseObserver.onCompleted()
  }
}
