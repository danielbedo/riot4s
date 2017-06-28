package com.github.danielbedo.riot4s.cache

import com.github.danielbedo.riot4s.cache.memory.MemoryCacheConfig
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class ServiceCacheComponentSpec extends FlatSpec with Matchers {

  "A Memory Cache" should "return a cached value" in {
    val memoryCache = new MemoryCache(MemoryCacheConfig())

    val cacheKey = "ck1"
    val cacheValue = "{\"someKey\": \"someValue\"}"
    val futureResult = for {
      _ <- memoryCache.putItem(cacheKey, cacheValue)
      retrieved <- memoryCache.getItem(cacheKey)
    } yield retrieved
    val result = Await.result(futureResult, 1 seconds)

    assert(result.isDefined && result.get == cacheValue)
  }

  "A Guava Cache" should "return None if the value is not present" in {
    val memoryCache = new MemoryCache(MemoryCacheConfig())

    val cacheKey = "ck1"
    val result = Await.result(memoryCache.getItem(cacheKey), 1 seconds)

    assert(result.isEmpty)
  }

  "A Noop Cache" should "not return anything" in {
    val noopCache = new NoopCache

    val cacheKey = "ck1"
    val cacheValue = "{\"someKey\": \"someValue\"}"
    val futureResult = for {
      _ <- noopCache.putItem(cacheKey, cacheValue)
      retrieved <- noopCache.getItem(cacheKey)
    } yield retrieved
    val result = Await.result(futureResult, 1 seconds)

    assert(result.isEmpty)
  }

}
