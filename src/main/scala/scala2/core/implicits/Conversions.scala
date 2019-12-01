package scala2.core.implicits
import scala.language.implicitConversions

object Conversions {
  implicit def int2Integer(i: Int):java.lang.Integer = 
    java.lang.Integer.valueOf(i)
}