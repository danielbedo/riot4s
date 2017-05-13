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
import cats.data.{EitherT, OptionT}
import cats.implicits._
import com.github.danielbedo.ApiErrors.{ApiError, BadRequest}

trait LeagueApiComponent {
  val leagueApi: LeagueApi

  trait LeagueApi {
    def get(url: String, queryParam: Map[String, String] = Map.empty): EitherT[Future, ApiError, String]
  }
}

trait DefaultLeagueApiComponent extends LeagueApiComponent {
  self: ActorSystemProvider with ServiceCacheComponent =>

  val apiKey: String
  val leagueApi = new DefaultLeagueApi(apiKey)

  class DefaultLeagueApi(apiKey: String) extends LeagueApi {
    implicit val materializer = ActorMaterializer()

    def get(url: String, queryParam: Map[String, String] = Map.empty): EitherT[Future, ApiError, String] = {
      val uri = Uri(url).withQuery(Query(queryParam + ("api_key" -> apiKey)))
      val cacheKey = Uri(url).withQuery(Query(queryParam)).toString   // we don't need the apiKey in the cacheKey

      EitherT.right(serviceCache.getItem(cacheKey)).flatMap { cacheItem =>
        cacheItem.fold(call(url, queryParam)) { hit =>
          EitherT.right[Future, ApiError, String](Future(hit))
        }
      }
    }

    private def call(url: String, queryParam: Map[String, String]): EitherT[Future, ApiError, String] = {
      val requestUri = Uri(url).withQuery(Query(queryParam + ("api_key" -> apiKey)))
      val cacheUri = Uri(url).withQuery(Query(queryParam)).toString

      val httpResponse = Http().singleRequest(HttpRequest(uri = requestUri)).flatMap { entity =>
        val r: Future[Either[ApiError, String]] = entity.status match {
          case StatusCodes.BadRequest => Future(Left(BadRequest))
          case StatusCodes.OK =>
            for {
              strictEntity <- entity.entity.toStrict(500.millis)
            } yield {
              val json = strictEntity.data.utf8String
              serviceCache.putItem(cacheUri, json)
              Right(json)
            }
        }
        r
      }
      EitherT(httpResponse)
    }

  }

}

