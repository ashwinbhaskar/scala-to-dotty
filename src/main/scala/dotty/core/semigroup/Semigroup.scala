package dotty
package core
package semigroup

/**
  Creating typeclass in dotty requires less boilerplate because of extension methods. 
  Creating a type class in dotty has 2 parts

  1 - Typeclass itself defined by the trait
  2 - givens for the type we care about for the type class
 
 */
trait Semigroup[A]:
  extension (a: A) def combine (b: A): A


// givens are the instances for the types you are interested in
object SemigroupInstances:
  given Semigroup[Int] with
    extension (a: Int) def combine (b: Int): Int = a + b

  given optionSemigroup[A : Semigroup]: Semigroup[Option[A]] with
    extension (a: Option[A]) def combine (b: Option[A]) = 
      (a, b) match 
        case (Some(aVal), Some(bVal)) => Some(aVal.combine(bVal))
        case (Some(aVal), None) => Some(aVal)
        case (None, Some(bVal)) => Some(bVal)
        case (None, None) => None