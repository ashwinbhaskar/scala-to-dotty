package dotty
package core
package semigroup

/**
  Creating typeclass in dotty requires less boilerplate because of extension methods. 
  Creating a type class in dotty has 2 parts

  1 - Typeclass itself defined by the trait
  2 - givens for the type we care about for the type class
 
 */
trait Semigroup[A] {
  def (a: A) combine (b: A): A
}

// givens are the instances for the types you are interested in
object SemigroupInstances{
  given IntAdditionSemigroup as Semigroup[Int] {
    def (a: Int) combine (b: Int): Int = a + b
  }

  given OptionSemigroup[A : Semigroup] as Semigroup[Option[A]] {
    def (a: Option[A]) combine (b: Option[A]) = (a, b) match {
      case (Some(aVal), Some(bVal)) => Some(aVal.combine(bVal))
      case (Some(aVal), None) => Some(aVal)
      case (None, Some(bVal)) => Some(bVal)
      case (None, None) => None
    }
  }
}