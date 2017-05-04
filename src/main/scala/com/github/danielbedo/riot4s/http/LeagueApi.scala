package com.github.danielbedo.riot4s.http

import com.github.danielbedo.riot4s.ActorSystemProvider
import com.github.danielbedo.riot4s.cache.ServiceCacheComponent

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import cats.data.OptionT
import cats.implicits._

trait LeagueApiComponent {
  def leagueApi: LeagueApi

  trait LeagueApi {
    def get(url: String, queryParam: Map[String, String] = Map.empty): Future[String]
  }

}

trait DefaultLeagueApiComponent extends LeagueApiComponent {
  self: ActorSystemProvider with ServiceCacheComponent =>

  val apiKey: String
  def leagueApi = new DefaultLeagueApi(apiKey)

  class DefaultLeagueApi(apiKey: String) extends LeagueApi {
    implicit val materializer = ActorMaterializer()

    def get(url: String, queryParam: Map[String, String] = Map.empty): Future[String] = {
      val uri = Uri(url).withQuery(Query(queryParam + ("api_key" -> apiKey)))
      val cacheKey = Uri(url).withQuery(Query(queryParam)).toString   // we don't need the apiKey in the cacheKey

      OptionT(serviceCache.getItem(cacheKey)).getOrElseF {
        println(s"cache miss -> retrieving '$cacheKey' from API")
        Http()
          .singleRequest(HttpRequest(uri = uri))
          .flatMap(elem => elem.entity.toStrict(500.millis))
          .map { elem =>
            val json = elem.data.utf8String
            serviceCache.putItem(cacheKey, json)
            json
          }
      }
    }

  }

}


