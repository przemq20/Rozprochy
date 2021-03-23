package Admin

import scala.io.StdIn.readLine

object AdminMain extends App {
  val admin = new Admin
  println("""Hello, first enter command (teams, suppliers, all), then type message, for example:
            |"all example message"
            |""".stripMargin)
  while (true) {
    val message = readLine
    val words   = message.split(" ")
    val command = words.head.toUpperCase
    val msg     = words.drop(1).mkString(" ")
    if (msg.nonEmpty) {
      command match {
        case "TEAMS" | "0"     => admin.sendToAllTeams(msg)
        case "SUPPLIERS" | "1" => admin.sendToAllSuppliers(msg)
        case "ALL" | "2"       => admin.sendToAll(msg)
        case _                 => scribe.error("Wrong command")
      }
    } else {
      scribe.warn("Provide command and message in one line.")
    }
  }
}
