val dottyVersion = "0.18.1-RC1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-to-dotty",
    version := "0.1.0",

    scalaVersion := dottyVersion,
  )