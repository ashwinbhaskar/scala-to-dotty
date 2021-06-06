package dotty
package core

/**
Extension Methods helps developers to add new methods to an existing type
without having to sub-class it or recompile the original type
*/

/** 
In dotty extension methods are simple and obvious and does not require any implicit class
*/

extension (value: Int)
  def square: Int = value * value

object ExtensionMethodsDemo:
  import dotty.core._

  val intVal = 13
  val squareVal = intVal.square
  println(s"Square of $intVal = $squareVal")