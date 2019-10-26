package dotty
package core

import semigroup.Semigroup
// To import a given we need to explicitly say that we are importing a given.
// Everything other than givens in the Semigroup object will be ignored
import semigroup.SemigroupInstances.{given Semigroup[Int], given Semigroup[Option[?]]}

//Just annotating a function with @main is enough
@main def semigroupDemo : Unit = {
  println(Option[Int](1).combine(Option[Int](2)))
}
