name := "zookepeer"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "org.apache.zookeeper" % "zookeeper" % "3.7.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.outr" %% "scribe" % "3.4.0"
)
