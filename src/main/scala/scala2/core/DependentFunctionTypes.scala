package scala2
package core

trait Foo{
  type Baz
  val baz : Baz
}

//This is dependant method type. This works.
def extractBaz(f : Foo) : f.Baz = f.baz

//But what if you wanted a function type for extractBax?
//That is not possible with Scala 2 because there is no type that could describe it.
//https://dotty.epfl.ch/docs/reference/new-types/dependent-function-types.html

val bazExtractor : (f : Foo) => f.Baz = extractBaz  // This will not compile with Scala 2