package scala2.core

import scala2.core.implicits.Conversions._
import scala.language.implicitConversions // use with care !
import shared.printInteger

// Look for difference in scala.core.implicits.Conversions.scala
object ImplicitsDemo extends App {
  val i = 1
  printInteger(i)
}