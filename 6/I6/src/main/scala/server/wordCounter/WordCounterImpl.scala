package server.wordCounter

import io.grpc.stub.StreamObserver
import wordCounter.{ Result, WordCountGrpc, Words }

import scala.collection.mutable

class WordCounterImpl extends WordCountGrpc.WordCountImplBase {

  val map: mutable.Map[String, Int] = mutable.Map[String, Int]()

  override def countWord(request: Words, responseObserver: StreamObserver[Result]): Unit = {
    val list = request.getValuesList
    scribe.info(s"Started Word Counting for $list")
    val map = mutable.Map[String, Int]()
    for (i <- 0 until list.size()) {
      map.update(request.getValues(i), map.getOrElse(request.getValues(i), 0) + 1)
      Thread.sleep(500)
      responseObserver.onNext(Result.newBuilder().setValue(map(request.getValues(i))).setKey(request.getValues(i)).build())
    }
    responseObserver.onCompleted()
  }
}
