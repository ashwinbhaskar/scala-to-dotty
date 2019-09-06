package dotty
package core

// To import a given we need to explicitly say that we are importing a given.
// Everything other than givens in the Semigroup object will be ignored
import given semigroup.SemigroupInstances._
object SemigroupDemo extends App {
  println(Option[Int](1).combine(Option[Int](2)))
}