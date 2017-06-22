package com.github.danielbedo.riot4s.builder

import com.github.danielbedo.riot4s.cache.{MemoryCache, NoopCache, ServiceCache}
import com.github.danielbedo.riot4s.cache.memory.MemoryCacheConfig

import scala.concurrent.duration.Duration

private class Api(serviceCache: ServiceCache)

case class ApiBuilder(serviceCache: Option[ServiceCache] = None) {

  /**
    * Add default Guava caching to your API.
    */
  def withMemoryCache(): ApiBuilder = withMemoryCache(MemoryCacheConfig())
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
  def withMemoryCache(guavaConfig: MemoryCacheConfig): ApiBuilder = this.copy(serviceCache = Some(new MemoryCache(guavaConfig)))



  def build(): Api = {
    new Api(
      serviceCache = serviceCache.getOrElse(new NoopCache)
    )
  }

}
