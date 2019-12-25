package dotty.core.implicits

/*Because the usage of Type Conversions often are very problematic,
 it must be created explicitly in this way:
 Also notice that the pattern matching. This is required because we have enabled the compiler option -Yexplicit-nulls
 This has an effect on java interop as well. All the reference types in java are nullable. To keep that consistent with the 
 Type System of Dotty, the java types are patched with `UncheckedNull`(Unchecked Null is a type alias for Null) 
 java.lang.Integer.valueOf(_) is returns Integer | UncheckedNull*/
given Conversion[Int, Integer] = java.lang.Integer.valueOf(_) match
    case a: java.lang.Integer => a
    case null => throw new Exception("Can't convert null to Integer value") 


// Long version, using alias given:
// given Conversion[Int, Integer] with
//  def apply(i: Int): Integer = java.lang.Integer.valueOf(i)