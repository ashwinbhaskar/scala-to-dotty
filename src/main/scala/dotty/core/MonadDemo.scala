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

