val dottyVersion = "0.16.0-RC3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-to-dotty",
    version := "0.1.0",

    scalaVersion := dottyVersion,
  )