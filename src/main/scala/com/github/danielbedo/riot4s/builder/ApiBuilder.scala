package com.github.danielbedo.riot4s.builder

import com.github.danielbedo.riot4s.Regions
import com.github.danielbedo.riot4s.Regions.Region
import com.github.danielbedo.riot4s.cache.{MemoryCache, NoopCache}
import com.github.danielbedo.riot4s.cache.memory.MemoryCacheConfig
import com.github.danielbedo.riot4s.http.DefaultLeagueApiComponent
import com.github.danielbedo.riot4s.ratelimit.{NoopRateLimiter, QueueRateLimiter}

import scala.concurrent.duration.Duration
import akka.actor.ActorSystem
import com.github.danielbedo.riot4s.ActorSystemProvider
import com.github.danielbedo.riot4s.cache.ServiceCache
import com.github.danielbedo.riot4s.ratelimit.RateLimiter
import com.github.danielbedo.riot4s.service.statsv13.RiotStatsServiceComponent
import com.github.danielbedo.riot4s.service.status.RiotStatusServiceComponent
import com.github.danielbedo.riot4s.service.summoner.RiotSummonerServiceComponent

class RiotApi(override val apiKey: String,
              override val serviceCache: ServiceCache,
              val rateLimiter: RateLimiter)(implicit as: ActorSystem)
  extends DefaultLeagueApiComponent
    with ActorSystemProvider
    with RiotStatusServiceComponent
    with RiotSummonerServiceComponent
    with RiotStatsServiceComponent {

  override val actorSystem: ActorSystem = as

}

case class ApiBuilder(apiKey: String,
                      serviceCache: ServiceCache = new NoopCache,
                      rateLimiter: RateLimiter = new NoopRateLimiter) {

  // Caching
  /**
    * Add in memory caching to your API. The cache works after the LRU principle after reaching
    * the given maximum size.
    *
    * @param maximumSize total size of the in memory cache.
    * @return ApiBuilder with configured caching layer
    */
  def withMemoryCache(maximumSize: Long): ApiBuilder = withMemoryCache(MemoryCacheConfig(maximumSize))
  def withMemoryCache(ttl: Option[Duration]): ApiBuilder = withMemoryCache(MemoryCacheConfig(ttl = ttl))
  def withMemoryCache(maximumSize: Long, ttl: Option[Duration]): ApiBuilder = withMemoryCache(MemoryCacheConfig(maximumSize, ttl))
  def withMemoryCache(guavaConfig: MemoryCacheConfig = MemoryCacheConfig()): ApiBuilder = this.copy(serviceCache = new MemoryCache(guavaConfig))

  // RateLimiter
  def withRateLimiter(regions: Set[Region] = Regions.regions): ApiBuilder = this.copy(rateLimiter = new QueueRateLimiter(regions))
  def withRateLimiter(region: Region): ApiBuilder = this.copy(rateLimiter = new QueueRateLimiter(Set(region)))

  def build = new RiotApi(apiKey, serviceCache, rateLimiter)

}
