package scala2.core

import scala2.core.monad.MonadInstances._
import scala2.core.monad.MonadInterfaceSyntax._  //with this import we will import everything..including the implicits
import scala2.core.monad.Monad
import share.MyClass

object MonadDemo extends App {

  def transform[A,B, F[_] : Monad](value : F[A])(func : A => F[B]) : F[B] = value.flatMap(func)
  
  val myClass : MyClass[Int] = new MyClass(1)
  val transformerFunc : Int => MyClass[Double] = {(a : Int) => new MyClass(a.toDouble)}
 // val result1 = transform(myClass, transformerFunc)
 // println("flatMap on MyClass")
 // println(result1)


}