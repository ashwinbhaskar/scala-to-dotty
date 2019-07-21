# Introduction

With dotty almost ready to be released, this project will compare the scala2 ways of doing things and the dotty way of implementing the same. By doing so we hope to learn and teach the differences / similarities between scala2 and dotty.

# Project Structure
```
src
  |-- main
        |-- scala
              |-- dotty
              |-- scala2
```

All the codes written in dotty will be inside dotty package whereas the codes written for scala2 will reside inside scala2 package.

# IDE

As of now dotty is officially supported in Visual Studio Code.

 1. Install the `Visual Studio application`.
 2. Install `Dotty Language Server` extension from the `marketplace` (Goto View -> Extensions)
 3. Uninstall `Metals` if installed
 4. Make sure to add `addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.3.3")` to your `plugins.sbt` as done in this project.
 5. All that remains now is telling `sbt` the version of Scala [Dotty in this case] that you want to use. At the time this readme was written
    `0.16.0-RC3` was the latest version. This will keep changing as more stable versions are released. You can track it here https://github.com/lampepfl/dotty/releases
