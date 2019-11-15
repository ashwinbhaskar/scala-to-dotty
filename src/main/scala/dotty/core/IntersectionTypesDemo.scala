import scala.concurrent.Future

/*
`&` is similar to `with` in scala 2 when only used as class composition
*/

trait RedisClient
    def increment(key: String): Future[Unit]

trait KafkaClient
    def push[A](topic: String, message: A): Future[Unit]

trait CustomerInfoClient
    type Name = String
    type Age = Int
    type Gender = String
    def getDetails(customerId: String): Future[(Name, Age, Gender)]
/*
 You can pass along `dependeny` to all the handlers. They will be able to accpe them as
 `CustomerInfoClient` or `KafkaClient` or `RedisClient`. This example is inspired from 
 John De Goes's article on the module pattern - http://degoes.net/articles/zio-environment#the-module-pattern
*/
def startServer(dependency: RedisClient & KafkaClient & CustomerInfoClient) = ???


/*
It's different from with in Scala 2 in the sense that `&` is commutative. Unlike with `with` in scala 2,
both (???: A & B).foo and (???: B & A).foo return type Int
*/

trait Base
    def foo: Any
trait A extends Base
    override def foo: Int = 1
trait B extends Base
    override def foo: Any = "foo"

def func(ab: A & B): Int = ab.foo //returns Int

def func(ba: B & A): Int = ba.foo //return Int

