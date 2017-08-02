
name := "play-scala"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "com.outworkers"  %% "phantom-dsl" % "2.11.2"
)

libraryDependencies += "org.scala-lang.modules" %% "scala-async" % "0.9.1"


