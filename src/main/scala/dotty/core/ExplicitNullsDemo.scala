package dotty
package core

@main def explicitNullsDemo: Unit = 

    /*
    Explicit Nulls is an opt-in feature which can be enabled with -Yexplicit-nulls compiler flag
    */
    val foo: String | Null = null
    //The statemented below will not compile because we have enabled the compiler flag -Yexplicit-nulls
    //val baz: String = null

    
    //val baz: java.lang.Integer = java.lang.Integer.valueOf(0) : This will not compile!
    /*
    How do you get java interop to work with -Yexplicit-nulls compiler flag?
    When a java class is loaded, either from source or bytecode, it's types are patched so that they remain nullable.
    The patching is done by making are referrence types nullable. UncheckedNull is a type alias for Null.*/
    val baz: java.lang.Integer | UncheckedNull = java.lang.Integer.valueOf(0)
