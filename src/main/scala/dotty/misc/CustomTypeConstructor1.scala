package misc

class MyClass[A](val value : A)
  override def toString = s"MyClass with type ${value.getClass} and value $value"