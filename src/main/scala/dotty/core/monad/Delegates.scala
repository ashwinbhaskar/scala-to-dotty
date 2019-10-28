package monad
import misc.types.Id
import misc.MyClass

given idMonad: Monad[Id]
  def (a : A) identity[A] : Id[A] = a

  def (a : Id[A]) flatMap[A,B](func : A => Id[B]) : Id[B] = func(a)


given myClassMonad: Monad[MyClass]
  def (a : A) identity[A] : MyClass[A] = MyClass(a)

  def (a : MyClass[A]) flatMap[A,B](func : A => MyClass[B]) : MyClass[B] = func(a.value)

