package scala2.core.implicits
import scala.language.implicitConversions

/*
The explicit null check is only done because we are compiling all the code with dotty with -Yexplicit-nulls flag enabled
*/
object Conversions {
  implicit def int2Integer(i: Int):java.lang.Integer = 
    java.lang.Integer.valueOf(i) match {
      case a: Integer => a
      case null => throw new Exception("Cannot convert null to integer value")
    }
}