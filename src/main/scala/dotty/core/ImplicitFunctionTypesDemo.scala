package dotty
package core

/*

Implicit Functions  are functions with only implicit parameters. Their type is `Implicit Function Type`.

The motivation for Implicit function types is to reduce boiler plate code on the declaration site when using implicits parameters. Martin Odersky's blog
(https://www.scala-lang.org/blog/2016/12/07/implicit-function-types.html) on the need for implicit function types explains the motivation well.

Implicit parameters, by nature, do not require you to `explicitely` pass them at the call site. This does reduce the boiler plate but 
we still end up with having to define the implicit parameters at the declaration site.

Implicit Function Types help you to remove that extra boiler plate of having to define the implicit parameters at the declaration site.
*/

//Example taken from https://dotty.epfl.ch/docs/reference/contextual/implicit-function-types.html

import scala.concurrent.ExecutionContext

type Executable[T] = (ec: ExecutionContext) ?=> T

/*
Note that you don't need to tell foo `explicitely` to accept an implicit ExecutionType.
*/
def foo(x: Int): Executable[Int] = ??? //The body of the function will have access to an ExecutionContext

/*
With implicit function types you are not restricted to have a `def` whenever you need an implicit argument.
NOTE - There is a hack in scala 2 to make the below work with a val. You can see it in the Scala Version
*/

type Environment = String

//The type of the function says that an implict Environment will be available
val adminIds: (ec: Environment) ?=> List[Int] = 
    if(summon[Environment] == "staging") List(1,2,3)
    else List(4,5,6)

@main def implicitFunctionTypes = 
    given Environment = "staging"
    println(adminIds)