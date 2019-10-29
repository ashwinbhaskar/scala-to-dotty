package dotty
package core

trait FooTrait[T](val foo: String){
  def fooMessage: String = foo
  def goo: T
}

class Goo extends FooTrait[Int](foo = "Hello Foo"){
  override def goo: Int = 1
}

@main def traitParametersDemo: Unit = println(Goo().fooMessage)

