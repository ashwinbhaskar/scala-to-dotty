package scala2
package core

/*
Having an implicit Function type is not possible in Scala2
*/

/*
This will NOT compile with scala 2
import scala.concurrent.ExecutionContext
type Executable[T] = (implicit a: ExecutionContext) => T
*/

//To implement what was implemented in the Dotty version we will have to do this
import scala.concurrent.ExecutionContext

/*We will need to `explicitely` tell that we need an implict of type ExecutionContext in the declaration site.
Imagine doing this in a large code base where in you might need to pass down implicits all the way down to the actual method using it.
That will force you to add the implicit parameters in the declaration site for all the methods propaging the implicit.
That definitely is boiler plate
*/
def foo(x: Int)(implicit ec: ExecutionContext): Int = ???

//In the Dotty version we spoke about a work around when assigning a function with implicit parameter to a val when using Scala 2
type Environment = String

//But the function type does not carry any information about the implicitness of the the parameters of the type `Environment`
val adminIds: Environment => List[Int] = {
    implicit env: Environment => if(env == "staging") List(1,2,3)
        else List(4,5,6)
}

object ImplicitFunctionTypes extends App{
    println("Making a function explitely of implicit function type is not possible in Scala 2")
}
