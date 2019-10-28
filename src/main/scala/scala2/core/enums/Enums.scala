package enums

/**
Enums help in compile time check for all possible values. There are couple of ways to have enums in Scala 2

1 - By extending scala's built in Enumeration class
2 - We can use sealed objects
*/

// Enumeration
object Week extends Enumeration {
  val Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday = Value
}

// Enumeration with custom values and ordering
object WeekCustom extends Enumeration {
  val Monday = Value(1, "Mon")
  val Tuesday = Value(2, "Tue")
  val Wednesday = Value(3, "Wed")
  val Thursday = Value(4, "Thur")
  val Friday = Value(5, "Fri")
  val Saturday = Value(6, "Sat")
  val Sunday = Value(0, "Sun")
}

// Sealed case objects

sealed trait WeekTrait
case object Monday extends WeekTrait
case object Tuesday extends WeekTrait
case object Wednesday extends WeekTrait
case object Thursday extends WeekTrait
case object Friday extends WeekTrait
case object Saturday extends WeekTrait
case object Sunday extends WeekTrait


// Sealed case object with extra fields
sealed abstract class WeekWithExtraFields(val abbreviation: String,
                                          val weekDay: Boolean,
                                          index: Int) {
  // User defined members
  def isWeekDay: Boolean = weekDay
                                          }
case object MondayWithFields extends WeekWithExtraFields("Mon", true, 1)
case object TuesdayWithFields extends WeekWithExtraFields("Tue", true, 2)
case object WednesdayWithFields extends WeekWithExtraFields("Wed", true, 3)
case object ThursdayWithFields extends WeekWithExtraFields("Thu", true, 4)
case object FridayWithFields extends WeekWithExtraFields("Fri", true, 5)
case object SaturdayWithFields extends WeekWithExtraFields("Sat", true, 6)
case object SundayWithFields extends WeekWithExtraFields("Sun", true, 0)

object EnumOps {
  /**
   Positives of Enumeration
    1 - Number of classes generated is less
    2 - You can list down all the possible values
    3 - You can also compare enum values. By default they are ordered by the order they are declared
  */
  
  def displayAllValues(): Unit = {
    val values = Week.values
    println(values)
  }

  // comparedValue will be true
  var comparedValue = Week.Monday < Week.Sunday

  // comparedValue will be false
  comparedValue = WeekCustom.Monday < WeekCustom.Sunday

  /**
    Negatives of Enumeration
    1 - No exhaustive matching check during compile time.
    2 - Enumeration have same type after erasure 
  */
  
  // No ehautive matching.. Below function does not return any warnings or failure at compile time
  def isWeekday(day: Week.Value) = day match {
    case Week.Monday => true
  }

  /** 
  Enumeration have same type after erasure 
  
  def test(day: Week.Value) = 
    println(day)
  
  def test(day: WeekCustom.Value) = 
    println(day)

  Declaring above two functions will result in an error  
  */

  // -- Sealed case objects

  /** 
    Postives of Sealed case objects
    1 - Exhaustive matching check at compile time
        Below function will cause a warning at compile time
        def isWeekday(day: Week.Value) = day match {
          case Week.Monday => true
        }
  
    2 - You can include more fields and user defined members on the enum values. Check `WeekWithExtraFields` for example

  */

  /** 
    Negatives of Sealed case objects
    
    1 - Compiler generates more classes than Enumeration
    2 - No simple way to retrieve all enum values
    3 - No default ordering between enum values. However we can do this manually, for example by using the index field.
  */
}