package dotty
package core

/*
Dotty allows traits to have parameters like you have in classes.
An important note: "Arguments to a trait are evaluated immediately before the trait is initialized"
*/
trait FooTrait[T](val foo: String){
  def fooMessage: String = foo
  def goo: T
}

/*
When a class extends a trait with parameters it has to pass in the parameter values
*/
class Goo extends FooTrait[Int](foo = "Hello from Foo"):
  override def goo: Int = 1

/*
Traits cannot pass arguments to parent traits
*/
trait BazTrait[T] extends FooTrait[T]:
  def baz: String

/*But how do you make a class extend BazTrait?
  class Zoo extends BazTrait[String] {
    override def goo: String = "Hello from Goo"\
    override def baz: String = "Hello from Baz"
  }
 This fails to compile with error "parameterized trait FooTrait is indirectly implemented, 
 needs to be implemented directly so that arguments can be passed one error found" 

 The correct way is to make Zoo extend both Greeting and Formal greeting in any order:(
*/

class Zoo extends FooTrait[String](foo = "Hello from Foo") with BazTrait[String]:
  override def goo: String = "Hello from Goo"
  override def baz: String = "Hello from Baz"

@main def traitParametersDemo: Unit = println(Goo().fooMessage)

