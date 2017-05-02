package example

import com.github.danielbedo.riot4s.{ActorSystemProvider, Regions}
import com.github.danielbedo.riot4s.http.DefaultLeagueApiComponent
import com.github.danielbedo.riot4s.cache.GuavaServiceCacheComponent
import com.github.danielbedo.riot4s.service.statsv13.DefaultStatsServiceComponent
import com.github.danielbedo.riot4s.service.status.DefaultStatusServiceComponent
import com.github.danielbedo.riot4s.service.summoner.DefaultSummonerServiceComponent

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorSystem

import cats._, cats.data._, cats.implicits._

object Hello extends App {
  println("Starting app")

  val services = new DefaultLeagueApiComponent
    with ActorSystemProvider
    with DefaultStatusServiceComponent
    with DefaultSummonerServiceComponent
    with DefaultStatsServiceComponent
    with GuavaServiceCacheComponent {
    override val actorSystem = ActorSystem()
    override val apiKey: String = ""
  }

  val statsFuture = for {
    summoner <-services.summonerService.getSummonerByName("", Regions.EUW)
    stats <- services.statsService.getSummary(summoner.id, Regions.EUW)
  } yield stats

  println(Await.result(statsFuture.value,Duration.Inf))



  services.actorSystem.shutdown()
}
