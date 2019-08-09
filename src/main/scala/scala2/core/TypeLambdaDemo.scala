package scala2.core

object TypeLambdaDemoScala extends App{
  trait Functor[F[_]]{
    def map[A,B](a : F[A])(func : A => B) : F[B]
  }

  def functionFunctor[X] = new Functor[({type T[A] = Function1[X,A]})#T]{ //A syntax difficult to understand
    def map[A,B](a : X => A)(func : A => B) : X => B = a andThen func
  }

  val stringToInt = (a : String) => a.toInt
  val intToDouble = (a : Int) => a.toDouble
 
  val composedFunction = functionFunctor[String].map(stringToInt)(intToDouble)

  println(composedFunction("1"))
}