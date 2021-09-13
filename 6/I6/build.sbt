name := "reverse_proxy_nginx"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "io.grpc" % "grpc-protobuf" % "1.38.0",
  "io.grpc" % "grpc-stub" % "1.38.0",
  "io.grpc" % "grpc-netty" % "1.38.0",
  "io.grpc" % "grpc-netty-shaded" % "1.38.0",
  "com.outr" %% "scribe" % "3.5.3"
)
