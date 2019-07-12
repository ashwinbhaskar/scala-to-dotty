package scala2
package core
package semigroup

/**
  Typeclass in scala2 usually had 3 parts. 
  1 - The typeclass itself defined by a trait.
  2 - Typeclass instances for the type we care for.
  3 - Exposing the functionality to the outside world. There are couple of ways of doing that
    (i) Interface object - To use this :  Semigroup.combine(a, b)
    (ii) Interface Syntax - Uses extension method to reduce the usage to - a.combine(b)  
 */

// 1- The typeclass itself defined by a trait.
trait Semigroup[A] {
  def combine(a: A, b: A): A
}

// 2 - Typeclass instances for the type we care for.
object SemigroupInstances {
  implicit val intAddSemigroup: Semigroup[Int] = new Semigroup[Int] {
    def combine(a: Int, b: Int): Int = a + b
  }

  implicit def optionSemigroup[A](implicit s: Semigroup[A]): Semigroup[Option[A]] = new Semigroup[Option[A]] {
    def combine(a: Option[A], b: Option[A]): Option[A] = (a, b) match {
      case (Some(aVal), Some(bVal)) => Some(s.combine(aVal, bVal))
      case (Some(aVal), None) => Some(aVal)
      case (None, Some(bVal)) => Some(bVal)
      case (None, None) => None
    }
  }
}

/**
  3 - Exposing the functionality to the outside world.
    (i) Interface object - To use this :  Semigroup.combine(a, b)
 */
object Semigroup {
  def combine[A](a: A, b: A)(implicit s: Semigroup[A]): A =
    s.combine(a, b)
  
}


// 3 (ii) - Interface Syntax - Uses extension method to reduce the usage to - a.combine(b)
object SemigroupSyntax {
  implicit class SemigroupOps[A](a: A) {
    def combine(b: A)(implicit s: Semigroup[A]): A =
      s.combine(a, b)
  }
}