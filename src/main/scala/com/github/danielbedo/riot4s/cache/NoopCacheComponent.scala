package com.github.danielbedo.riot4s.cache

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait NoopCacheComponent extends ServiceCacheComponent {
  val serviceCache = new NoopCache

  class NoopCache extends ServiceCache {

    def getItem(uri: String): Future[Option[String]] = {
      Future(None)
    }

    def putItem(uri: String, json: String): Future[Unit] = {
      Future(())
    }

  }

}