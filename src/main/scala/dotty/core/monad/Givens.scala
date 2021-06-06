package monad
import shared.Id
import shared.MyClass

given Monad[Id] with
  extension [A](a : A) def identity : Id[A] = a

  extension [A, B](a : Id[A]) def flatMap(func : A => Id[B]) : Id[B] = func(a)


given Monad[MyClass] with
  extension  [A](a : A) def identity : MyClass[A] = MyClass(a)

  extension [A,B](a : MyClass[A]) def flatMap(func : A => MyClass[B]) : MyClass[B] = func(a.value)

