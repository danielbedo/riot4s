package com.github.danielbedo.riot4s.service.summoner

import cats.data.EitherT
import com.github.danielbedo.ApiErrors.{ApiError, UnparseableJson}
import com.github.danielbedo.riot4s.Regions.Region
import com.github.danielbedo.riot4s.http.LeagueApiComponent
import com.github.danielbedo.riot4s.service.summoner.model.SummonerDTO

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import io.circe.Error
import io.circe.generic.auto._
import io.circe.parser._
import cats.implicits._

import scala.util.Random

trait FakeSummonerServiceComponent extends SummonerServiceComponent {
  self: LeagueApiComponent =>

  def summonerService = new FakeSummonerService

  class FakeSummonerService extends SummonerService {
    def getSummonerByName(summonerName: String, region: Region): EitherT[Future, ApiError, SummonerDTO] = {
      val fakeResponse =
        """
          |{
          |    "profileIconId": 582,
          |    "name": "--summonerName--",
          |    "summonerLevel": 30,
          |    "accountId": 24180799,
          |    "id": "--summonerId--",
          |    "revisionDate": 1493858818000
          |}
        """
          .stripMargin
          .replace("--summonerName--", summonerName)
          .replace("--summonerId--", Random.nextInt(10000).toString)

      val decoded = Future { decode[SummonerDTO](fakeResponse) }

      EitherT(decoded).leftMap { circeError =>
        val apiError: ApiError = UnparseableJson
        apiError
      }
    }
  }

}
