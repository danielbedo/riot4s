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
import com.github.danielbedo.ApiErrors

trait SummonerServiceComponent {
  val summonerService: SummonerService

  trait SummonerService {
    def getSummonerByName(summonerName: String, region: Region): EitherT[Future, ApiError, SummonerDTO]
    def getSummonerByAccountId(accountId: Long, region: Region): EitherT[Future, ApiError, SummonerDTO]
    def getSummonerBySummonerId(accountId: Long, region: Region): EitherT[Future, ApiError, SummonerDTO]
  }
}

trait RiotSummonerServiceComponent extends SummonerServiceComponent {
  self: LeagueApiComponent =>

  val summonerService = new RiotSummonerService

  class RiotSummonerService extends SummonerService {
    def getUrlForSummonerName(summonerName: String, region: Region) = s"${region.host}/lol/summoner/v3/summoners/by-name/$summonerName"
    def getUrlForAccountId(accountId: Long, region: Region) = s"${region.host}/lol/summoner/v3/summoners/by-account/$accountId"
    def getUrlForSummonerId(summonerId: Long, region: Region) = s"${region.host}/lol/summoner/v3/summoners/by-summoner/$summonerId"

    def getSummonerByName(summonerName: String, region: Region): EitherT[Future, ApiError, SummonerDTO] = {
      val url = getUrlForSummonerName(summonerName, region)
      getSummoner(url)
    }

    def getSummonerByAccountId(accountId: Long, region: Region): EitherT[Future, ApiError, SummonerDTO] = {
      val url = getUrlForAccountId(accountId, region)
      getSummoner(url)
    }

    def getSummonerBySummonerId(summonerId: Long, region: Region): EitherT[Future, ApiError, SummonerDTO] = {
      val url = getUrlForSummonerId(summonerId, region)
      getSummoner(url)
    }

    private def getSummoner(url: String) = {
      for {
        response <- makeApiCallAndMapError(url)
        summonerData <- decodeSummonerAndMapError(response)
      } yield summonerData
    }

    private def makeApiCallAndMapError(url: String): EitherT[Future, ApiErrors.ApiError, String] = {
      leagueApi.get(url).leftMap { error =>
        val leagueError: ApiError = error match {
          case NotFound => SummonerNotFound
          case err: ApiError => err
        }
        leagueError
      }
    }

    private def decodeSummonerAndMapError(jsonResponse: String): EitherT[Future, ApiErrors.ApiError, SummonerDTO] = {
      EitherT.fromEither[Future](decode[SummonerDTO](jsonResponse)).leftMap{ err =>
        val newErr: ApiError = UnparseableJson
        newErr
      }
    }

  }

}
