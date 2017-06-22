package com.github.danielbedo.riot4s.cache

import com.github.danielbedo.riot4s.cache.memory.MemoryCacheConfig
import com.google.common.cache.CacheBuilder

import scalacache._
import scalacache.guava._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

sealed trait ServiceCache {
  def getItem(uri: String): Future[Option[String]]
  def putItem(uri: String, json: String): Future[Unit]
}

class MemoryCache(config: MemoryCacheConfig) extends ServiceCache {
  val underlyingGuavaCache = CacheBuilder.newBuilder().maximumSize(config.maximumSize).build[String, Object]
  implicit val scalaCache = ScalaCache(GuavaCache(underlyingGuavaCache))
  val cache = typed[String, NoSerialization]

  def getItem(uri: String): Future[Option[String]] = {
    cache.get(uri)
  }

  def putItem(uri: String, json: String): Future[Unit] = {
    cache.put(uri)(json, ttl = config.ttl)
  }

}

class NoopCache extends ServiceCache {

  def getItem(uri: String): Future[Option[String]] = {
    Future(None)
  }

  def putItem(uri: String, json: String): Future[Unit] = {
    Future(())
  }

}
