package example

import akka.actor.ActorSystem
import com.github.danielbedo.riot4s.Regions
import com.github.danielbedo.riot4s.builder.ApiBuilder

object BuilderExample {

  def main(args: Array[String]): Unit = {
    implicit val actorSystem = ActorSystem()
    val api = ApiBuilder("some-api-key")
      .withMemoryCache(100L)
      .withRateLimiter(Regions.EUW)
      .build
  }

}
