name := "Ekipa"

version := "0.1"

scalaVersion := "2.13.5"

//https://github.com/NewMotion/akka-rabbitmq
//https://github.com/spray/spray-json
//https://github.com/lightbend/scala-logging
libraryDependencies ++= Seq("com.newmotion" %% "akka-rabbitmq" % "6.0.0",
  "io.spray" %% "spray-json" % "1.3.6",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.outr" %% "scribe" % "3.4.0"
)
