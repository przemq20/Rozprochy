package Team

import scala.io.StdIn.readLine

//Main class of Team
object TeamMain extends App {
  //set team details
  println(s"Provide your team name:")
  val teamName = readLine.toUpperCase.filterNot(_.isWhitespace)
  val team     = new Team(teamName)

  //request equipment
  println(s"Provide your orders:")
  while (true) {
    val line      = readLine
    val equipment = line.toUpperCase.filterNot(_.isWhitespace)
    team.createOrder(equipment)
  }
}
