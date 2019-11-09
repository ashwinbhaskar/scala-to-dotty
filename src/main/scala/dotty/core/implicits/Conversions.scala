package dotty.core.implicits

// Because the usage of Type Conversions often are very problematic,
// it must be created explicitly in this way:
given Conversion[Int, Integer] with
  def apply(i: Int): Integer = java.lang.Integer.valueOf(i)


// Short version, using alias given:
// given Conversion[Int, Integer] = java.lang.Integer.valueOf(x)