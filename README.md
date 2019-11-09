# Introduction ![Build Status](https://travis-ci.org/ashwinbhaskar/scala-to-dotty.svg?branch=master) [![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=Checkout%20Dotty%20way%20of%20writing%20Scala2%20code&url=https://github.com/ashwinbhaskar/scala-to-dotty)

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

{2:5}(build.sbt)
@[scala](build.sbt)

### SemigroupDemo.scala

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

import semigroup.Semigroup
// To import a given we need to explicitly say that we are importing a given.
// Everything other than givens in the Semigroup object will be ignored
import semigroup.SemigroupInstances.{given Semigroup[Int], given Semigroup[Option[?]]}

//Just annotating a function with @main is enough
@main def semigroupDemo : Unit =
  println(1.combine(2)) 
  println(Option[Int](1).combine(Option[Int](2)))

```
### TraitParametersDemo.scala

**Scala2 version**
```scala
package scala2
package core

/*
Compiling FooTrait with Scala 2 will give the error:
trait Foo(val a: String){
                ^
       error: traits or objects may not have parameters

trait FooTrait(val foo: String){
  def fooMessage: String = foo
  def goo: T
}
*/

object TraitParametersDemo extends App{
  println("trait parameters are not possible with Scala 2")
}
```
**Dotty version**
```scala
package dotty
package core

/*
Dotty allows traits to have parameters like you have in classes.
An important note: "Arguments to a trait are evaluated immediately before the trait is initialized"
*/
trait FooTrait[T](val foo: String){
  def fooMessage: String = foo
  def goo: T
}

/*
When a class extends a trait with parameters it has to pass in the parameter values
*/
class Goo extends FooTrait[Int](foo = "Hello from Foo")
  override def goo: Int = 1

/*
Traits cannot pass arguments to parent traits
*/
trait BazTrait[T] extends FooTrait[T]
  def baz: String

/*But how do you make a class extend BazTrait?
  class Zoo extends BazTrait[String] {
    override def goo: String = "Hello from Goo"\
    override def baz: String = "Hello from Baz"
  }
 This fails to compile with error "parameterized trait FooTrait is indirectly implemented, 
 needs to be implemented directly so that arguments can be passed one error found" 

 The correct way is to make Zoo extend both Greeting and Formal greeting in any order:(
*/

class Zoo extends FooTrait[String](foo = "Hello from Foo") with BazTrait[String]
  override def goo: String = "Hello from Goo"
  override def baz: String = "Hello from Baz"

@main def traitParametersDemo: Unit = println(Goo().fooMessage)


```
### Enums.scala

**Scala2 version**
```scala
package enums

/**
Enums help in compile time check for all possible values. There are couple of ways to have enums in Scala 2

1 - By extending scala's built in Enumeration class
2 - We can use sealed objects
*/

// Enumeration
object Week extends Enumeration {
  val Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday = Value
}

// Enumeration with custom values and ordering
object WeekCustom extends Enumeration {
  val Monday = Value(1, "Mon")
  val Tuesday = Value(2, "Tue")
  val Wednesday = Value(3, "Wed")
  val Thursday = Value(4, "Thur")
  val Friday = Value(5, "Fri")
  val Saturday = Value(6, "Sat")
  val Sunday = Value(0, "Sun")
}

// Sealed case objects

sealed trait WeekTrait
case object Monday extends WeekTrait
case object Tuesday extends WeekTrait
case object Wednesday extends WeekTrait
case object Thursday extends WeekTrait
case object Friday extends WeekTrait
case object Saturday extends WeekTrait
case object Sunday extends WeekTrait


// Sealed case object with extra fields
sealed abstract class WeekWithExtraFields(val abbreviation: String,
                                          val weekDay: Boolean,
                                          index: Int) {
  // User defined members
  def isWeekDay: Boolean = weekDay
                                          }
case object MondayWithFields extends WeekWithExtraFields("Mon", true, 1)
case object TuesdayWithFields extends WeekWithExtraFields("Tue", true, 2)
case object WednesdayWithFields extends WeekWithExtraFields("Wed", true, 3)
case object ThursdayWithFields extends WeekWithExtraFields("Thu", true, 4)
case object FridayWithFields extends WeekWithExtraFields("Fri", true, 5)
case object SaturdayWithFields extends WeekWithExtraFields("Sat", true, 6)
case object SundayWithFields extends WeekWithExtraFields("Sun", true, 0)

object EnumOps {
  /**
   Positives of Enumeration
    1 - Number of classes generated is less
    2 - You can list down all the possible values
    3 - You can also compare enum values. By default they are ordered by the order they are declared
  */
  
  def displayAllValues(): Unit = {
    val values = Week.values
    println(values)
  }

  // comparedValue will be true
  var comparedValue = Week.Monday < Week.Sunday

  // comparedValue will be false
  comparedValue = WeekCustom.Monday < WeekCustom.Sunday

  /**
    Negatives of Enumeration
    1 - No exhaustive matching check during compile time.
    2 - Enumeration have same type after erasure 
  */
  
  // No ehautive matching.. Below function does not return any warnings or failure at compile time
  def isWeekday(day: Week.Value) = day match {
    case Week.Monday => true
  }

  /** 
  Enumeration have same type after erasure 
  
  def test(day: Week.Value) = 
    println(day)
  
  def test(day: WeekCustom.Value) = 
    println(day)

  Declaring above two functions will result in an error  
  */

  // -- Sealed case objects

  /** 
    Postives of Sealed case objects
    1 - Exhaustive matching check at compile time
        Below function will cause a warning at compile time
        def isWeekday(day: Week.Value) = day match {
          case Week.Monday => true
        }
  
    2 - You can include more fields and user defined members on the enum values. Check `WeekWithExtraFields` for example

  */

  /** 
    Negatives of Sealed case objects
    
    1 - Compiler generates more classes than Enumeration
    2 - No simple way to retrieve all enum values
    3 - No default ordering between enum values. However we can do this manually, for example by using the index field.
  */
}
```
**Dotty version**
```scala
package dotty
package core
package enums

enum Week
  case Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday

// Parameterized enums

enum WeekWithFields(val abbreviation: String)
  case Monday extends WeekWithFields("Mon")
  case Tuesday extends WeekWithFields("Tue")
  case Wednesday extends WeekWithFields("Wed")
  case Thursday extends WeekWithFields("Thur")
  case Friday extends WeekWithFields("Fri")
  case Saturday extends WeekWithFields("Sat")
  case Sunday extends WeekWithFields("Sun")



// User Defined members
// Java Planet example - https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
enum Planet(mass: Double, radius: Double)
  // User defined members
  private final val G = 6.67300E-11
  def surfaceGravity = G * mass / (radius * radius)
  def surfaceWeight(otherMass: Double) =  otherMass * surfaceGravity

  case Mercury extends Planet(3.303e+23, 2.4397e6)
  case Venus   extends Planet(4.869e+24, 6.0518e6)
  case Earth   extends Planet(5.976e+24, 6.37814e6)
  case Mars    extends Planet(6.421e+23, 3.3972e6)
  case Jupiter extends Planet(1.9e+27,   7.1492e7)
  case Saturn  extends Planet(5.688e+26, 6.0268e7)
  case Uranus  extends Planet(8.686e+25, 2.5559e7)
  case Neptune extends Planet(1.024e+26, 2.4746e7)


object EnumOps 
  // to get the unique index of an enum value

  def printOrdinal(day: Week) = 
    val ordinal = day.ordinal
    println(s"$day has ordinal = $ordinal")

  /**
    Positives
    
    1 - You can list down all the possible values.
    2 - User defined members
    3 - Clear syntax
   */
 
  def displayAllValues(): Unit = 
    val values: Array[Week] = Week.values
    println(values)
  
  // User defined members
  def weightInDifferentPlanet(weightOnEarth: Float, planet: Planet): Unit = 
    val mass = weightOnEarth / Planet.Earth.surfaceGravity
    println(s"Your weight on $planet is ${planet.surfaceWeight(mass)}")
  
  /** 
  Negatives
  1 - No default ordering of enum values and hence cannot be compared.
  */

```
### ExtensionMethods.scala

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

object RichExtensions
  def (value: Int) square: Int = value * value

object ExtensionMethodsDemo
  import RichExtensions._

  val intVal = 13
  val squareVal = intVal.square
  println(s"Square of $intVal = $squareVal")
```
### MonadDemo.scala

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
  val transformerFunc : Int => MyClass[Double] = {(a : Int) => new MyClass(a.toDouble)}



}
```
**Dotty version**
```scala
package dotty
package core

import monad.Monad
import misc.types.Id
import misc.MyClass
import monad.{given Monad[Id]}      //Import given only
import monad.{given Monad[MyClass]}


def transform[A,B,F[_] : Monad](value : F[A], transformerFunc : A => F[B]) : F[B] = value.flatMap(transformerFunc)

//Just annotating a function with @main is enough
@main def monadDemo : Unit = 
  val myClassObj : MyClass[Int] = MyClass(1)
  val transformerMyClass : Int => MyClass[Double] = {(a : Int) => MyClass(a.toDouble)}
  val result1 = transform(myClassObj, transformerMyClass)
  println("flatMap on MyClass")
  println(result1)

  val ida : Id[Int] = 1
  val transformerId : Int => Double = _.toDouble
  val result2 = transform(ida, transformerId)
  println("flatMap on Id")
  println(result2)


```
### Semigroup.scala

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
trait Semigroup[A]
  def (a: A) combine (b: A): A


// givens are the instances for the types you are interested in
object SemigroupInstances
  given intAdditionSemigroup: Semigroup[Int]
    def (a: Int) combine (b: Int): Int = a + b

  given optionSemigroup[A : Semigroup]:  Semigroup[Option[A]]
    def (a: Option[A]) combine (b: Option[A]) = 
      (a, b) match 
        case (Some(aVal), Some(bVal)) => Some(aVal.combine(bVal))
        case (Some(aVal), None) => Some(aVal)
        case (None, Some(bVal)) => Some(bVal)
        case (None, None) => None
```
### TypeLambdaDemo.scala

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

//Annotating a function with @main is enough

@main def typeLambdaDemo : Unit = 
  trait Functor[F[_]]
    def map[A,B](a : F[A])(func : A => B) : F[B]

  def functionFunctor[X] = 
    //Much cleaner syntax compared to Scala
    new Functor[[A] =>> Function1[X,A]] with
      def map[A,B](a : X =>A)(func : A => B) : X => B = a andThen func
  

  val stringToInt = (a : String) => a.toInt
  val intToDouble = (a : Int) => a.toDouble
 
  val composedFunction = functionFunctor[String].map(stringToInt)(intToDouble)

  println(composedFunction("1"))
```
### Monad.scala

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

trait Monad[F[_]]
  def [A](a : A) identity : F[A]

  def [A,B](a : F[A]) flatMap(func : A => F[B]) : F[B]

  def [A,B](a : F[A]) map(func : A => B) : F[B] = a.flatMap(func andThen identity)

```
### DependentFunctionTypesDemo.scala

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

trait Foo
  type Baz
  val baz : Baz

// This is dependent method type. This works in Scala 2 as well
def extractBaz(f : Foo) : f.Baz = f.baz

// But what if you wanted a function type for extractBaz?
// It was not possible to do that in Scala 2.
// But with dependent function types introduced in dotty, this is possible.
// Read more about dependent function types here
// https://dotty.epfl.ch/docs/reference/new-types/dependent-function-types.html

val bazExtractor : (f : Foo) => f.Baz = extractBaz

```
