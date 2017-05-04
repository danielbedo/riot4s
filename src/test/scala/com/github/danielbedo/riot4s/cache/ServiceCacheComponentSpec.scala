package com.github.danielbedo.riot4s.cache

import com.github.danielbedo.riot4s.http.EmptyLeagueApiComponent
import com.github.danielbedo.riot4s.service.summoner.FakeSummonerServiceComponent
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class ServiceCacheComponentSpec extends FlatSpec with Matchers {
  val services = new EmptyLeagueApiComponent
    with FakeSummonerServiceComponent
    with GuavaServiceCacheComponent

  "A Guava Cache" should "return a cached value" in {
    val cacheKey = "ck1"
    val cacheValue = "{\"someKey\": \"someValue\"}"
    val futureResult = for {
      _ <- services.serviceCache.putItem(cacheKey, cacheValue)
      retrieved <- services.serviceCache.getItem(cacheKey)
    } yield retrieved
    val result = Await.result(futureResult, 1 seconds)

    assert (result.isDefined && result.get == cacheValue)
  }

  "A Guava Cache" should "return None if the value is not present" in {
    val cacheKey = "ck1"
    val result = Await.result(services.serviceCache.getItem(cacheKey), 1 seconds)

    assert(result.isEmpty)
  }

}
