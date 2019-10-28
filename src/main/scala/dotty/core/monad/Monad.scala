package monad

trait Monad[F[_]]
  def (a : A) identity[A] : F[A]

  def (a : F[A]) flatMap[A,B](func : A => F[B]) : F[B]

  def (a : F[A]) map[A,B](func : A => B) : F[B] = a.flatMap(func andThen identity)
