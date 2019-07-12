package scala2
package core

/**
  To use the semigroup typeclass we import the sytax and the instances of type we care about 
 */
import scala2.core.semigroup._
import semigroup.SemigroupInstances._
import semigroup.SemigroupSyntax._

object SemigroupDemo extends App {
  //using Interface Syntax
  println(1.combine(2))
  println(Option[Int](1).combine(Option[Int](2)))

  //using interface object
  Semigroup.combine(1, 2)
}