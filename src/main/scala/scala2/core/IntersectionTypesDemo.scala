package scala2
package core

import scala.concurrent.Future
trait RedisClient {
    def increment(key: String): Future[Unit]
}

trait KafkaClient {
    def push[A](topic: String, message: A): Future[Unit]
}

trait CustomerInfoClient {
    type Name = String
    type Age = Int
    type Gender = String
    def getDetails(customerId: String): Future[(Name, Age, Gender)]
}

def startServer(dependency: RedisClient with KafkaClient with CustomerInfoClient) = ???


/* But `with` is not commutative */
trait Base {
    def foo: Any
}
trait A extends Base {
    override def foo: Int = 1
}
trait B extends Base {
    override def foo: Any = "foo"
}



def func(ab: A with B): Int = ab.foo // This will not compile with Scala2

def func(ba: B with A): Int = ba.foo    //return Int