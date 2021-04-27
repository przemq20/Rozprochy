name := "satellites"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.14",
  "com.typesafe.akka" %% "akka-slf4j" % "2.6.14",
  "com.outr" %% "scribe" % "3.4.0",
  "org.slf4j" % "slf4j-simple" % "1.7.30",
  "org.slf4j" % "slf4j-api" % "1.7.30"
)
