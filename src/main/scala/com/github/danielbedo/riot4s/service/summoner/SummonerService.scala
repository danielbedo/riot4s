package com.github.danielbedo.riot4s.service.summoner

import cats.data.EitherT
import com.github.danielbedo.ApiErrors._
import com.github.danielbedo.riot4s.Regions.Region
import com.github.danielbedo.riot4s.http.LeagueApiComponent
import com.github.danielbedo.riot4s.service.summoner.model.SummonerDTO
import io.circe.Error

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import io.circe.generic.auto._
import io.circe.parser._
import cats.implicits._

trait SummonerServiceComponent {
  def summonerService: SummonerService

  trait SummonerService {
    def getSummonerByName(summonerName: String, region: Region): EitherT[Future, ApiError, SummonerDTO]
  }
}

trait DefaultSummonerServiceComponent extends SummonerServiceComponent {
  self: LeagueApiComponent =>

  def summonerService = new DefaultSummonerService

  class DefaultSummonerService extends SummonerService {

    def getSummonerByName(summonerName: String, region: Region): EitherT[Future, ApiError, SummonerDTO] = {
      val url = s"${region.host}/lol/summoner/v3/summoners/by-name/$summonerName"

      for {
        response <- leagueApi.get(url).leftMap { error =>
          val leagueError: ApiError = error match {
            case NotFound => SummonerNotFound
            case err: ApiError => err
          }
          leagueError
        }
        summonerData <- EitherT.fromEither[Future](decode[SummonerDTO](response)).leftMap{ err =>
          val newErr: ApiError = UnparseableJson
          newErr
        }
      } yield summonerData

    }

  }

}
