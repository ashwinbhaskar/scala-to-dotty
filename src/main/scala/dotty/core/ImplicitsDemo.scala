package dotty.core

import implicits.given 
import scala.language.implicitConversions
import shared.printInteger

@main def implicitsDemo : Unit =
  val i = 1
  printInteger(i)
  