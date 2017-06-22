package example

import com.github.danielbedo.riot4s.{Regions}
import com.github.danielbedo.riot4s.util.ApiBuilder

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorSystem

import cats._, cats.data._, cats.implicits._


object Hello extends App {
  println("Starting app")
  val key = "your-api-key-here"

  implicit val actorSystem = ActorSystem()
  val api = ApiBuilder(key).build()

  val statsFuture = for {
    summoner <-api.summonerService.getSummonerByName("some-summoner", Regions.EUW)
    stats <- api.statsService.getSummary(summoner.id, Regions.EUW)
  } yield stats

  println(Await.result(statsFuture.value,Duration.Inf))

  actorSystem.terminate()
}

