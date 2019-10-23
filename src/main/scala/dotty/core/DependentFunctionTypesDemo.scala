package dotty
package core

trait Foo{
  type Baz
  val baz : Baz
}

// This is dependent method type. This works in Scala 2 as well
def extractBaz(f : Foo) : f.Baz = f.baz

// But what if you wanted a function type for extractBaz?
// It was not possible to do that in Scala 2.
// But with dependent function types introduced in dotty, this is possible.
// Read more about dependent function types here
// https://dotty.epfl.ch/docs/reference/new-types/dependent-function-types.html

val bazExtractor : (f : Foo) => f.Baz = extractBaz
