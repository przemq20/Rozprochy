package Utils

import satellite.Status

import scala.collection.mutable

case class Query(
  var queryId:    Int,
  var left:       Int,
  var errors:     mutable.Map[Int, Status],
  var successful: Int,
  var range:      Int
)
