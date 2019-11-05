package monad

trait Monad[F[_]]
  def [A](a : A) identity : F[A]

  def [A,B](a : F[A]) flatMap(func : A => F[B]) : F[B]

  def [A,B](a : F[A]) map(func : A => B) : F[B] = a.flatMap(func andThen identity)
