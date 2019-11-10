package scala2.core

import scala2.core.implicits.Conversions._
import scala.language.implicitConversions
import shared.printInteger

object ImplicitsDemo extends App {
  val i = 1
  printInteger(i)
}