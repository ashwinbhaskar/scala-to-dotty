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