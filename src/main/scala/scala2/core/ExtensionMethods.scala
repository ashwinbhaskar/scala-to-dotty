package scala2
package core

/**
Extension Methods helps developers to add new methods to an existing type
without having to sub-class it or recompile the original type
*/

/** 
In scala2 extension methods were implemented using the implicit class 
approach
*/

object RichExtensions {
  implicit class RichInt(value: Int) {
    def square = value * value
  }
}

object ExtensionMethodsDemo {
  import RichExtensions._

  val intVal = 13
  val squareVal = intVal.square
  println(s"Square of $intVal = $squareVal")
}