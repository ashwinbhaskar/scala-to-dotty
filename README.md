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


### MonadDemo.scala

**Scala2 version**
```scala
package scala2.core

import scala2.core.monad.MonadInstances._
import scala2.core.monad.MonadInterfaceSyntax._  //with this import we will import everything..including the implicits
import scala2.core.monad.Monad
import shared.MyClass

object MonadDemo extends App {

  def transform[A,B, F[_] : Monad](value : F[A])(func : A => F[B]) : F[B] = value.flatMap(func)
  
  val myClass : MyClass[Int] = new MyClass(1)
  val transformerFunc : Int => MyClass[Double] = {(a : Int) => new MyClass(a.toDouble)}
 // val result1 = transform(myClass, transformerFunc)
 // println("flatMap on MyClass")
 // println(result1)


}
```
**Dotty version**
```scala
package dotty.core

import monad.Monad
import shared.Id
import shared.MyClass
import monad.{given Monad[Id]}      //Import given only
import monad.{given Monad[MyClass]}


def transform[A,B,F[_] : Monad](value : F[A], func : A => F[B]) : F[B] = value.flatMap(func)

//Just annotating a function with @main is enough
@main def monadDemo : Unit = 
  val myClass : MyClass[Int] = MyClass(1)
  val transformerFunc : Int => MyClass[Double] = {(a : Int) => MyClass(a.toDouble)}
  val result1 = transform(myClass, transformerFunc)
  println("flatMap on MyClass")
  println(result1)

  val ida : Id[Int] = 1
  val transformerId : Int => Double = _.toDouble
  val result2 = transform(ida, transformerId)
  println("flatMap on Id")
  println(result2)


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

trait Foo:
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
class Goo extends FooTrait[Int](foo = "Hello from Foo"):
  override def goo: Int = 1

/*
Traits cannot pass arguments to parent traits
*/
trait BazTrait[T] extends FooTrait[T]:
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

class Zoo extends FooTrait[String](foo = "Hello from Foo") with BazTrait[String]:
  override def goo: String = "Hello from Goo"
  override def baz: String = "Hello from Baz"

@main def traitParametersDemo: Unit = println(Goo().fooMessage)


```
### Monad.scala

**Scala2 version**
```scala
package scala2.core.monad
import shared.MyClass



trait Monad[F[_]]{
  def pure[A](a : A) : F[A]

  def flatMap[A,B](value : F[A])(func : A => F[B]) : F[B]

  def map[A,B](value : F[A])(func : A => B) : F[B] = flatMap(value)(func andThen pure)
}

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

trait Monad[F[_]]:
  extension [A](a: A) def identity : F[A]

  extension [A, B](a : F[A]) def flatMap(func : A => F[B]) : F[B]

  extension [A, B](a : F[A]) def map(func : A => B) : F[B] = a.flatMap(func andThen identity)

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

extension (value: Int)
  def square: Int = value * value

object ExtensionMethodsDemo:
  import dotty.core._

  val intVal = 13
  val squareVal = intVal.square
  println(s"Square of $intVal = $squareVal")
```
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
### IntersectionTypesDemo.scala

**Scala2 version**
```scala
package scala2
package core

import scala.concurrent.Future
trait RedisClient {
    def increment(key: String): Future[Unit]
}

trait KafkaClient {
    def push[A](topic: String, message: A): Future[Unit]
}

trait CustomerInfoClient {
    type Name = String
    type Age = Int
    type Gender = String
    def getDetails(customerId: String): Future[(Name, Age, Gender)]
}

def startServer(dependency: RedisClient with KafkaClient with CustomerInfoClient) = ???

/* But `with` is not commutative */
trait Base {
    def foo: Any
}
trait A extends Base {
    override def foo: Int = 1
}
trait B extends Base {
    override def foo: Any = "foo"
}

def func(ab: A with B): Int = ab.foo // This will not compile with Scala2

// def func(ba: B with A): Int = ba.foo
```
**Dotty version**
```scala
import scala.concurrent.Future

/*
`&` is similar to `with` in scala 2 when only used as class composition
*/

trait RedisClient:
    def increment(key: String): Future[Unit]

trait KafkaClient:
    def push[A](topic: String, message: A): Future[Unit]

trait CustomerInfoClient:
    def getName(customerId: String): Future[String]
/*
 You can pass along `dependency` to all the handlers. They will be able to accept them as
 `CustomerInfoClient` or `KafkaClient` or `RedisClient`.
*/
def startServer(dependency: RedisClient & KafkaClient & CustomerInfoClient) = ???

/*
It's different from with in Scala 2 in the sense that `&` is commutative. Unlike with `with` in scala 2,
both (???: A & B).foo and (???: B & A).foo return type Int
*/

trait Base:
    def foo: Any
trait A extends Base:
    override def foo: Int = 1
trait B extends Base:
    override def foo: Any = "foo"

def func(ab: A & B): Int = ab.foo //returns Int

//def func(ba: B & A): Int = ba.foo //is same as the above and compilation fails


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

enum Week:
  case Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday

// Parameterized enums

enum WeekWithFields(val abbreviation: String):
  case Monday extends WeekWithFields("Mon")
  case Tuesday extends WeekWithFields("Tue")
  case Wednesday extends WeekWithFields("Wed")
  case Thursday extends WeekWithFields("Thur")
  case Friday extends WeekWithFields("Fri")
  case Saturday extends WeekWithFields("Sat")
  case Sunday extends WeekWithFields("Sun")



// User Defined members
// Java Planet example - https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
enum Planet(mass: Double, radius: Double):
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


object EnumOps:
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
  trait Functor[F[_]]:
    def map[A,B](a : F[A])(func : A => B) : F[B]

  def functionFunctor[X] = 
    //Much cleaner syntax compared to Scala
    new Functor[[A] =>> Function1[X,A]]:
      def map[A,B](a : X =>A)(func : A => B) : X => B = a andThen func
  

  val stringToInt = (a : String) => a.toInt
  val intToDouble = (a : Int) => a.toDouble
 
  val composedFunction = functionFunctor[String].map(stringToInt)(intToDouble)

  println(composedFunction("1"))
```
### ImplicitFunctionTypesDemo.scala

**Scala2 version**
```scala
package scala2
package core

/*
Having an implicit Function type is not possible in Scala2
*/

/*
This will NOT compile with scala 2
import scala.concurrent.ExecutionContext
type Executable[T] = (implicit a: ExecutionContext) => T
*/

//To implement what was implemented in the Dotty version we will have to do this
import scala.concurrent.ExecutionContext

/*We will need to `explicitely` tell that we need an implict of type ExecutionContext in the declaration site.
Imagine doing this in a large code base where in you might need to pass down implicits all the way down to the actual method using it.
That will force you to add the implicit parameters in the declaration site for all the methods propaging the implicit.
That definitely is boiler plate
*/
def foo(x: Int)(implicit ec: ExecutionContext): Int = ???

//In the Dotty version we spoke about a work around when assigning a function with implicit parameter to a val when using Scala 2
type Environment = String

//But the function type does not carry any information about the implicitness of the the parameters of the type `Environment`
val adminIds: Environment => List[Int] = {
    implicit env: Environment => if(env == "staging") List(1,2,3)
        else List(4,5,6)
}

object ImplicitFunctionTypes extends App{
    println("Making a function explitely of implicit function type is not possible in Scala 2")
}

```
**Dotty version**
```scala
package dotty
package core

/*

Implicit Functions  are functions with only implicit parameters. Their type is `Implicit Function Type`.

The motivation for Implicit function types is to reduce boiler plate code on the declaration site when using implicits parameters. Martin Odersky's blog
(https://www.scala-lang.org/blog/2016/12/07/implicit-function-types.html) on the need for implicit function types explains the motivation well.

Implicit parameters, by nature, do not require you to `explicitely` pass them at the call site. This does reduce the boiler plate but 
we still end up with having to define the implicit parameters at the declaration site.

Implicit Function Types help you to remove that extra boiler plate of having to define the implicit parameters at the declaration site.
*/

//Example taken from https://dotty.epfl.ch/docs/reference/contextual/implicit-function-types.html

import scala.concurrent.ExecutionContext

type Executable[T] = (ec: ExecutionContext) ?=> T

/*
Note that you don't need to tell foo `explicitely` to accept an implicit ExecutionType.
*/
def foo(x: Int): Executable[Int] = ??? //The body of the function will have access to an ExecutionContext

/*
With implicit function types you are not restricted to have a `def` whenever you need an implicit argument.
NOTE - There is a hack in scala 2 to make the below work with a val. You can see it in the Scala Version
*/

type Environment = String

//The type of the function says that an implict Environment will be available
val adminIds: (ec: Environment) ?=> List[Int] = 
    if(summon[Environment] == "staging") List(1,2,3)
    else List(4,5,6)

@main def implicitFunctionTypes = 
    given Environment = "staging"
    println(adminIds)
```
### Conversions.scala

**Scala2 version**
```scala
package scala2.core.implicits
import scala.language.implicitConversions

/*
The explicit null check is only done because we are compiling all the code with dotty with -Yexplicit-nulls flag enabled
*/
object Conversions {
  implicit def int2Integer(i: Int):java.lang.Integer = 
    java.lang.Integer.valueOf(i) match {
      case a: Integer => a
      case null => throw new Exception("Cannot convert null to integer value")
    }
}
```
**Dotty version**
```scala
package dotty.core.implicits

/*Because the usage of Type Conversions often are very problematic,
 it must be created explicitly in this way:
 Also notice that the pattern matching. This is required because we have enabled the compiler option -Yexplicit-nulls
 This has an effect on java interop as well. All the reference types in java are nullable. To keep that consistent with the 
 Type System of Dotty, the java types are patched with `UncheckedNull`(Unchecked Null is a type alias for Null) 
 java.lang.Integer.valueOf(_) is returns Integer | UncheckedNull*/
given Conversion[Int, Integer] = java.lang.Integer.valueOf(_) match
    case a: java.lang.Integer => a
    case null => throw new Exception("Can't convert null to Integer value") 


// Long version, using alias given:
// given Conversion[Int, Integer] with
//  def apply(i: Int): Integer = java.lang.Integer.valueOf(i)
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
```
### ImplicitsDemo.scala

**Scala2 version**
```scala
package scala2.core

import scala2.core.implicits.Conversions._
import scala.language.implicitConversions // use with care !
import shared.printInteger

// Look for difference in scala.core.implicits.Conversions.scala
object ImplicitsDemo extends App {
  val i = 1
  printInteger(i)
}
```
**Dotty version**
```scala
package dotty.core

import implicits.{given}
import scala.language.implicitConversions // use with care !
import shared.printInteger

// Look for difference in dotty.core.implicits.Conversions.scala
@main def implicitsDemo : Unit =
  val i = 1
  printInteger(i)
  
```
### ExplicitNullsDemo.scala

**Scala2 version**
```scala
package scala2
package core

object ExplicitNullsDemo extends App{
    println("Scala2 does not have explicit nulls feature")
}
```
**Dotty version**
```scala
package dotty
package core

@main def explicitNullsDemo: Unit = 

    /*
    Explicit Nulls is an opt-in feature which can be enabled with -Yexplicit-nulls compiler flag
    */
    val foo: String | Null = null
    //The statemented below will not compile because we have enabled the compiler flag -Yexplicit-nulls
    //val baz: String = null

    
    //val baz: java.lang.Integer = java.lang.Integer.valueOf(0) : This will not compile!
    /*
    How do you get java interop to work with -Yexplicit-nulls compiler flag?
    When a java class is loaded, either from source or bytecode, it's types are patched so that they remain nullable.
    The patching is done by making are referrence types nullable. UncheckedNull is a type alias for Null.*/
    val baz: java.lang.Integer | Null = java.lang.Integer.valueOf(0)

```
