import scala.concurrent.Future

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