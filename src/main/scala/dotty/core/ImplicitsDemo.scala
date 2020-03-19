package dotty.core

import implicits.{given _}
import scala.language.implicitConversions // use with care !
import shared.printInteger

// Look for difference in dotty.core.implicits.Conversions.scala
@main def implicitsDemo : Unit =
  val i = 1
  printInteger(i)
  