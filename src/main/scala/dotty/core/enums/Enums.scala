package dotty
package core
package enums

enum Week
  case Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday

// Parameterized enums

enum WeekWithFields(val abbreviation: String)
  case Monday extends WeekWithFields("Mon")
  case Tuesday extends WeekWithFields("Tue")
  case Wednesday extends WeekWithFields("Wed")
  case Thursday extends WeekWithFields("Thur")
  case Friday extends WeekWithFields("Fri")
  case Saturday extends WeekWithFields("Sat")
  case Sunday extends WeekWithFields("Sun")



// User Defined members
// Java Planet example - https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
enum Planet(mass: Double, radius: Double)
  // User defined members
  private final val G = 6.67300E-11
  def surfaceGravity = G * mass / (radius * radius)
  def surfaceWeight(otherMass: Double) =  otherMass * surfaceGravity

  case Mercury extends Planet(3.303e+23, 2.4397e6)
  case Venus   extends Planet(4.869e+24, 6.0518e6)
  case Earth   extends Planet(5.976e+24, 6.37814e6)
  case Mars    extends Planet(6.421e+23, 3.3972e6)
  case Jupiter extends Planet(1.9e+27,   7.1492e7)
  case Saturn  extends Planet(5.688e+26, 6.0268e7)
  case Uranus  extends Planet(8.686e+25, 2.5559e7)
  case Neptune extends Planet(1.024e+26, 2.4746e7)


object EnumOps 
  // to get the unique index of an enum value

  def printOrdinal(day: Week) = 
    val ordinal = day.ordinal
    println(s"$day has ordinal = $ordinal")

  /**
    Positives
    
    1 - You can list down all the possible values.
    2 - User defined members
    3 - Clear syntax
   */
 
  def displayAllValues(): Unit = 
    val values: Array[Week] = Week.values
    println(values)
  
  // User defined members
  def weightInDifferentPlanet(weightOnEarth: Float, planet: Planet): Unit = 
    val mass = weightOnEarth / Planet.Earth.surfaceGravity
    println(s"Your weight on $planet is ${planet.surfaceWeight(mass)}")
  
  /** 
  Negatives
  1 - No default ordering of enum values and hence cannot be compared.
  */
