package monad
import shared.Id
import shared.MyClass

given Monad[Id]:
  def [A](a : A) identity : Id[A] = a

  def [A,B](a : Id[A]) flatMap(func : A => Id[B]) : Id[B] = func(a)


given Monad[MyClass]:
  def [A](a : A) identity : MyClass[A] = MyClass(a)

  def [A,B](a : MyClass[A]) flatMap(func : A => MyClass[B]) : MyClass[B] = func(a.value)

