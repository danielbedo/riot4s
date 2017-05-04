package com.github.danielbedo.riot4s.http

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * A Fake "Api Caller" Component that always returns an empty json
  */
trait EmptyLeagueApiComponent extends LeagueApiComponent {

  def leagueApi = new EmptyLeagueApi

  class EmptyLeagueApi extends LeagueApi {

    def get(url: String, queryParam: Map[String, String] = Map.empty): Future[String] = Future("{}")

  }

}