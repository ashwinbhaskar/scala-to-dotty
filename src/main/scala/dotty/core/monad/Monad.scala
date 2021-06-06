package monad

trait Monad[F[_]]:
  extension [A](a: A) def identity : F[A]

  extension [A, B](a : F[A]) def flatMap(func : A => F[B]) : F[B]

  extension [A, B](a : F[A]) def map(func : A => B) : F[B] = a.flatMap(func andThen identity)
