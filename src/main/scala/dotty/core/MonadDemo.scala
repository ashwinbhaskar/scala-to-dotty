object MonadDemo extends App{
  
  import given monad.IdMonad      //Import given only
  import given monad.MyClassMonad
  import monad.Monad
  
  import misc.types.Id
  import misc.MyClass

  def transform[A,B,F[_] : Monad](value : F[A], transformerFunc : A => F[B]) : F[B] = value.flatMap(transformerFunc)

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