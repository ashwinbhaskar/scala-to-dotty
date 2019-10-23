# Introduction

With dotty almost ready to be released, this project will compare the scala2 ways of doing things and the dotty way of implementing the same. By doing so we hope to learn and teach the differences / similarities between scala2 and dotty.

# Project Structure
```
src
  |-- main
        |-- scala
              |-- dotty
              |-- scala2
```

All the codes written in dotty will be inside dotty package whereas the codes written for scala2 will reside inside scala2 package.

# IDE

As of now dotty is officially supported in Visual Studio Code.

 1. Install the `Visual Studio application`.
 2. Install `Dotty Language Server` extension from the `marketplace` (Goto View -> Extensions)
 3. Uninstall `Metals` if installed
 4. Make sure to add `addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.3.3")` to your `plugins.sbt` as done in this project.
 5. All that remains now is telling `sbt` the version of Scala [Dotty in this case] that you want to use. At the time this readme was written
    `0.16.0-RC3` was the latest version. This will keep changing as more stable versions are released. You can track it here https://github.com/lampepfl/dotty/releases

# Side-by-side comparisons


#DependentFunctionTypesDemo.scala
**Scala2 version**
```scala
package scala2
package core

trait Foo{
  type Baz
  val baz : Baz
}

//This is dependant method type. This works.
def extractBaz(f : Foo) : f.Baz = f.baz

//But what if you wanted a function type for extractBax?
//That is not possible with Scala 2 because there is no type that could describe it.
//https://dotty.epfl.ch/docs/reference/new-types/dependent-function-types.html

val bazExtractor : (f : Foo) => f.Baz = extractBaz  // This will not compile with Scala 2
```
**Dotty version**
```scala
package dotty
package core

trait Foo{
  type Baz
  val baz : Baz
}

//This is dependant method type. This works in Scala 2 as well
def extractBaz(f : Foo) : f.Baz = f.baz

//But what if you wanted a function type for extractBax?
//It was not possible to do that in Scala 2
//But with dependant function types introduced in dotty, this is possible
//Read more about dependant function types here
//https://dotty.epfl.ch/docs/reference/new-types/dependent-function-types.html

val bazExtractor : (f : Foo) => f.Baz = extractBaz
```
#TypeLambdaDemo.scala
**Scala2 version**
```scala
package scala2.core

object TypeLambdaDemoScala extends App{
  trait Functor[F[_]]{
    def map[A,B](a : F[A])(func : A => B) : F[B]
  }

  def functionFunctor[X] = new Functor[({type T[A] = Function1[X,A]})#T]{ //Syntax difficult to understand
    def map[A,B](a : X => A)(func : A => B) : X => B = a andThen func
  }

  val stringToInt = (a : String) => a.toInt
  val intToDouble = (a : Int) => a.toDouble
 
  val composedFunction = functionFunctor[String].map(stringToInt)(intToDouble)

  println(composedFunction("1"))
}
```
**Dotty version**
```scala
package dotty
package core

//Just annotating a function with @main is enough

@main def typeLambdaDemo : Unit = {
  trait Functor[F[_]]{  
    def map[A,B](a : F[A])(func : A => B) : F[B]
  }

  def functionFunctor[X] = new Functor[[A] =>> Function1[X,A]] { //Much cleaner syntax compared to Scala
    def map[A, B](a : X =>A)(func : A => B) : X => B = a andThen func
  }

  val stringToInt = (a : String) => a.toInt
  val intToDouble = (a : Int) => a.toDouble
 
  val composedFunction = functionFunctor[String].map(stringToInt)(intToDouble)

  println(composedFunction("1"))

}
```
#Monad.scala
**Scala2 version**
```scala
package scala2.core.monad



trait Monad[F[_]]{
  def pure[A](a : A) : F[A]

  def flatMap[A,B](value : F[A])(func : A => F[B]) : F[B]

  def map[A,B](value : F[A])(func : A => B) : F[B] = flatMap(value)(func andThen pure)
}

import scala2.core.misc.MyClass
object MonadInstances{
  implicit def myclassMonadInstance : Monad[MyClass] = new Monad[MyClass]{
    def pure[A](a : A) : MyClass[A] = new MyClass(a)

    def flatMap[A,B](value : MyClass[A])(func : A => MyClass[B]) : MyClass[B] = func(value.value)
  }
}

object MonadInterfaceSyntax{
  implicit class MonadOps[F[_] : Monad, A](value : F[A]){
    def flatMap[B](func : A => F[B]) : F[B] = implicitly[Monad[F]].flatMap(value)(func)
  }
}
```
**Dotty version**
```scala
package monad

trait Monad[F[_]]{
  def (a : A) identity[A] : F[A]

  def (a : F[A]) flatMap[A,B](func : A => F[B]) : F[B]

  def (a : F[A]) map[A,B](func : A => B) : F[B] = a.flatMap(func andThen identity)
}
```
#Semigroup.scala
**Scala2 version**
```scala
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
```
**Dotty version**
```scala
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
```
#ExtensionMethods.scala
**Scala2 version**
```scala
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
```
**Dotty version**
```scala
package dotty
package core

/**
Extension Methods helps developers to add new methods to an existing type
without having to sub-class it or recompile the original type
*/

/** 
In dotty extension methods are simple and obvious and does not require any implicit class
*/

object RichExtensions {
  def (value: Int) square: Int = value * value
}

object ExtensionMethodsDemo {
  import RichExtensions._

  val intVal = 13
  val squareVal = intVal.square
  println(s"Square of $intVal = $squareVal")
}
```
#SemigroupDemo.scala
**Scala2 version**
```scala
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
```
**Dotty version**
```scala
package dotty
package core

// To import a given we need to explicitly say that we are importing a given.
// Everything other than givens in the Semigroup object will be ignored
import given semigroup.SemigroupInstances._

//Just annotating a function with @main is enough
@main def semigroupDemo : Unit = {
  println(Option[Int](1).combine(Option[Int](2)))
}

```
#MonadDemo.scala
**Scala2 version**
```scala
package scala2.core

object MonadDemo extends App{

  import scala2.core.monad.MonadInstances._
  import scala2.core.monad.MonadInterfaceSyntax._  //with this import we will import everything..including the implicits
  import scala2.core.monad.Monad
  import scala2.core.misc.MyClass


  def transform[A,B, F[_] : Monad](value : F[A])(func : A => F[B]) : F[B] = value.flatMap(func)
  val value : MyClass[Int] = new MyClass(1)
  val transformerFunc : Int => MyClass[Double] = {a : Int => new MyClass(a.toDouble)}



}
```
**Dotty version**
```scala
package dotty
package core

import given monad.IdMonad      //Import given only
import given monad.MyClassMonad
import monad.Monad
import misc.types.Id
import misc.MyClass


def transform[A,B,F[_] : Monad](value : F[A], transformerFunc : A => F[B]) : F[B] = value.flatMap(transformerFunc)

//Just annotating a function with @main is enough
@main def monadDemo : Unit = {
  val myClassObj : MyClass[Int] = MyClass(1)
  val transformerMyClass : Int => MyClass[Double] = {a : Int => MyClass(a.toDouble)}
  val result1 = transform(myClassObj, transformerMyClass)
  println("flatMap on MyClass")
  println(result1)

  val ida : Id[Int] = 1
  val transformerId : Int => Double = _.toDouble
  val result2 = transform(ida, transformerId)
  println("flatMap on Id")
  println(result2)
}


```
