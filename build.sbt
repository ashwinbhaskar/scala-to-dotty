val dottyVersion = "3.0.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-to-dotty",
    version := "0.1.0",
    scalacOptions ++= Seq(
      "-Yexplicit-nulls"
    ),
    scalaVersion := dottyVersion,
  )
