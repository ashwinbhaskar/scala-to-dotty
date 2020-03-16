package dotty
package core

//Annotating a function with @main is enough

@main def typeLambdaDemo : Unit = 
  trait Functor[F[_]]:
    def map[A,B](a : F[A])(func : A => B) : F[B]

  def functionFunctor[X] = 
    //Much cleaner syntax compared to Scala
    new Functor[[A] =>> Function1[X,A]] with
      def map[A,B](a : X =>A)(func : A => B) : X => B = a andThen func
  

  val stringToInt = (a : String) => a.toInt
  val intToDouble = (a : Int) => a.toDouble
 
  val composedFunction = functionFunctor[String].map(stringToInt)(intToDouble)

  println(composedFunction("1"))