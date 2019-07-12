package dotty
package core

// To import a delegate we need to explicitly say that we are importing a delegate.
// Everything other than delegates in the Semigroup object will be ignored
import delegate semigroup.SemigroupInstances._
object SemigroupDemo extends App {
  println(Option[Int](1).combine(Option[Int](2)))
}