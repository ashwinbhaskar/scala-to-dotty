package dotty.core

import implicits.given 
import scala.language.implicitConversions

def printInteger(i:java.lang.Integer):Unit = 
  println(s"I am an java.lang.Integer: ${i.toString}")

@main def implicitsDemo : Unit =
  val i = 1
  printInteger(i)
  