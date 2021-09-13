package client

import arithmetic.ArithmeticOpResult
import streamingProto.{ Number => GrpcNumber }
import io.grpc.stub.StreamObserver
import wordCounter.Result

object Observers {
    class ArithmeticObserver extends StreamObserver[ArithmeticOpResult] {
        override def onNext(value: ArithmeticOpResult): Unit = println(s"Result: ${value.getRes}")
        override def onError(t:    Throwable):          Unit = scribe.error(t.getMessage)
        override def onCompleted(): Unit = println("Result returned successfully")
    }

    class PrimeNumbersObserver extends StreamObserver[GrpcNumber] {
        override def onNext(value: GrpcNumber): Unit = println(s"Found Prime: ${value.getValue}")
        override def onError(t:    Throwable):  Unit = scribe.error(t.getMessage);
        override def onCompleted(): Unit = println("Streaming completed successfully")
    }

    class ResultObserver extends StreamObserver[Result] {
        override def onNext(value: Result):    Unit = println(s"$value")
        override def onError(t:    Throwable): Unit = scribe.error(t.getMessage);
        override def onCompleted(): Unit = println("Counting Words completed successfully")
    }
}
