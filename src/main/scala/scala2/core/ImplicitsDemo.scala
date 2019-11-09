package scala2.core

import scala2.core.implicits.Conversions._
import scala.language.implicitConversions

object ImplicitsDemo extends App {
  val i = 1
  printInteger(i)

  def printInteger(i:java.lang.Integer):Unit =
    println(s"I am an java.lang.Integer: ${i.toString}")
}