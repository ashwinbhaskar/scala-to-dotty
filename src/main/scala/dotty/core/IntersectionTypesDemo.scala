import scala.concurrent.Future

/*
`&` is similar to `with` in scala 2 when only used as class composition
*/

trait RedisClient:
    def increment(key: String): Future[Unit]

trait KafkaClient:
    def push[A](topic: String, message: A): Future[Unit]

trait CustomerInfoClient:
    def getName(customerId: String): Future[String]
/*
 You can pass along `dependency` to all the handlers. They will be able to accept them as
 `CustomerInfoClient` or `KafkaClient` or `RedisClient`.
*/
def startServer(dependency: RedisClient & KafkaClient & CustomerInfoClient) = ???

/*
It's different from with in Scala 2 in the sense that `&` is commutative. Unlike with `with` in scala 2,
both (???: A & B).foo and (???: B & A).foo return type Int
*/

trait Base:
    def foo: Any
trait A extends Base:
    override def foo: Int = 1
trait B extends Base:
    override def foo: Any = "foo"

def func(ab: A & B): Int = ab.foo //returns Int

def func(ba: B & A): Int = ba.foo //return Int

