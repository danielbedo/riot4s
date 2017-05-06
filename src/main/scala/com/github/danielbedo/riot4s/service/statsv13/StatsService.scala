package com.github.danielbedo.riot4s.service.statsv13

import cats.data.EitherT
import com.github.danielbedo.riot4s.Regions.Region
import com.github.danielbedo.riot4s.http.LeagueApiComponent
import com.github.danielbedo.riot4s.service.statsv13.model.PlayerStatsSummaryListDto
import io.circe._
import io.circe.parser._
import cats.syntax.either._
import com.github.danielbedo.ApiErrors.{ApiError, NotFound, SummonerNotFound, UnparseableJson}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import io.circe.generic.auto._
import cats.implicits._

trait StatsServiceComponent {
  def statsService: StatsService

  trait StatsService {
    def getSummary(summonerId: Long, region: Region): EitherT[Future, ApiError, PlayerStatsSummaryListDto]
  }

}

trait DefaultStatsServiceComponent extends StatsServiceComponent {
  self: LeagueApiComponent =>

  def statsService = new DefaultStatsService

  class DefaultStatsService extends StatsService{

    def getSummary(summonerId: Long, region: Region): EitherT[Future, ApiError, PlayerStatsSummaryListDto] = {
      val url = s"${region.host}/api/lol/EUW/v1.3/stats/by-summoner/$summonerId/summary"
      for {
        response <- leagueApi.get(url).leftMap { error =>
          val leagueError: ApiError = error match {
            case NotFound => SummonerNotFound
            case err: ApiError => err
          }
          leagueError
        }
        summonerData <- EitherT.fromEither[Future](decode[PlayerStatsSummaryListDto](response)).leftMap{ err =>
          val newErr: ApiError = UnparseableJson
          newErr
        }

      } yield summonerData
    }

  }

}