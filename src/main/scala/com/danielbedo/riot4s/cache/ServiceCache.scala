package com.danielbedo.riot4s.cache

import com.google.common.cache.CacheBuilder

import scalacache._
import guava._
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

trait ServiceCacheComponent {
  def serviceCache: ServiceCache

  trait ServiceCache {
    def getItem(uri: String): Future[Option[String]]
    def putItem(uri: String, json: String): Future[Unit]
  }

}

trait GuavaServiceCacheComponent extends ServiceCacheComponent {
  def serviceCache = new DefaultGuavaCache

  val underlyingGuavaCache = CacheBuilder.newBuilder().maximumSize(10000L).build[String, Object]
  implicit val scalaCache = ScalaCache(GuavaCache(underlyingGuavaCache))
  val cache = typed[String, NoSerialization]

  class DefaultGuavaCache extends ServiceCache {

    def getItem(uri: String): Future[Option[String]] = {
      cache.get(uri)
    }

    def putItem(uri: String, json: String): Future[Unit] = {
      cache.put(uri)(json, ttl = Some(3600.seconds))
    }

  }

}