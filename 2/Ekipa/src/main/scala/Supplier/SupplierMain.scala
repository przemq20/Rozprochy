package Supplier

import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn.readLine
import scala.Console.{ BOLD, RED, RESET }

object SupplierMain extends App {
  val buffer: ArrayBuffer[String] = new ArrayBuffer[String]()

  println("Provide supplier name:")
  val supplierName = readLine.toUpperCase.filterNot(_.isWhitespace)

  println("Enter supported equipment:")

  var done = false
  var in   = readLine
  while (in != "" && !done) {
    if (!buffer.contains(in)) {
      buffer.addOne(in.toUpperCase.filterNot(_.isWhitespace))
    } else {
      scribe.warn(s"Order type $in is already supported by this supplier!")
    }
    println(s"$RESET$BOLD${RED}Do you want to continue input data? [y/n]$RESET")
    val decision = readLine
    if (decision == "y") {
      in = readLine
    } else {
      done = true
    }
  }

  scribe.info(s"Supported equipment: ${buffer.mkString(", ")}")
  val supplier = new Supplier(buffer, supplierName)
}
