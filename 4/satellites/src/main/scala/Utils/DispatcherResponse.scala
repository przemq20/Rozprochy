package Utils

import satellite.Status

case class DispatcherResponse(
  queryId:    Int,
  errors:     Map[Int, Status],
  successful: Int
) extends Command
