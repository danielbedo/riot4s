package com.github.danielbedo.riot4s.http

import cats.data.EitherT
import com.github.danielbedo.ApiErrors.ApiError

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import cats.implicits._

/**
  * A Fake "Api Caller" Component that always returns an empty json
  */
trait EmptyLeagueApiComponent extends LeagueApiComponent {

  def leagueApi = new EmptyLeagueApi

  class EmptyLeagueApi extends LeagueApi {

    def get(url: String, queryParam: Map[String, String] = Map.empty): EitherT[Future, ApiError, String] =
      EitherT.right[Future, ApiError, String](Future(""))

  }

}