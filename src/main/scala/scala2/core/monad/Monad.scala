package scala2.core.monad
import scala2.core.misc.MyClass



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