package scala2
package core

/*
Compiling FooTrait with Scala 2 will give the error:
trait Foo(val a: String){
                ^
       error: traits or objects may not have parameters

trait FooTrait(val foo: String){
  def fooMessage: String = foo
  def goo: T
}
*/

object TraitParametersDemo extends App{
  println("trait parameters are not possible with Scala 2")
}