package com.github.danielbedo.riot4s.service.statsv13

import cats.data.EitherT
import com.github.danielbedo.riot4s.Regions.Region
import com.github.danielbedo.riot4s.http.LeagueApiComponent
import com.github.danielbedo.riot4s.service.statsv13.model.PlayerStatsSummaryListDto
import io.circe._
import io.circe.parser._
import cats.syntax.either._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import io.circe.generic.auto._

trait StatsServiceComponent {
  def statsService: StatsService

  trait StatsService {
    def getSummary(summonerId: Long, region: Region): EitherT[Future, Error, PlayerStatsSummaryListDto]
  }

}

trait DefaultStatsServiceComponent extends StatsServiceComponent {
  self: LeagueApiComponent =>

  def statsService = new DefaultStatsService

  class DefaultStatsService extends StatsService{

    def getSummary(summonerId: Long, region: Region): EitherT[Future, Error, PlayerStatsSummaryListDto] = {
      val url = s"https://euw.api.riotgames.com/api/lol/EUW/v1.3/stats/by-summoner/$summonerId/summary"
      val decoded = leagueApi
        .getAsString(url)
        .map { jsonString =>
        decode[PlayerStatsSummaryListDto](jsonString)
      }
      EitherT(decoded)
    }

  }

}