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

  val summonerService = new FakeSummonerService

  class FakeSummonerService extends SummonerService {

    def getSummonerByName(summonerName: String, region: Region): EitherT[Future, ApiError, SummonerDTO] = {
      val fakeResponse = getDummySummonerForName(summonerName)
      decodeResponse(fakeResponse)
    }

    def getSummonerByAccountId(accountId: Long, region: Region): EitherT[Future, ApiError, SummonerDTO] = {
      val fakeResponse = getDummySummonerForAccountId(accountId)
      decodeResponse(fakeResponse)
    }

    def getSummonerBySummonerId(summonerId: Long, region: Region): EitherT[Future, ApiError, SummonerDTO] = {
      val fakeResponse = getDummySummonerForSummonerId(summonerId)
      decodeResponse(fakeResponse)
    }

    private def decodeResponse(jsonString: String): EitherT[Future, ApiError, SummonerDTO] = {
      val decoded = Future { decode[SummonerDTO](jsonString) }

      EitherT(decoded).leftMap { circeError =>
        val apiError: ApiError = UnparseableJson
        apiError
      }
    }

    private def getDummySummonerForName(summonerName: String): String = {
      """
        |{
        |    "profileIconId": 582,
        |    "name": "--summonerName--",
        |    "summonerLevel": 30,
        |    "accountId": 24180799,
        |    "id": --summonerId--,
        |    "revisionDate": 1493858818000
        |}
      """
        .stripMargin
        .replace("--summonerName--", summonerName)
        .replace("--summonerId--", Random.nextInt(10000).toString)
    }

    private def getDummySummonerForAccountId(accountId: Long): String = {
      """
        |{
        |    "profileIconId": 582,
        |    "name": SomeRandomSummoner,
        |    "summonerLevel": 30,
        |    "accountId": --accountId--,
        |    "id": --summonerId--,
        |    "revisionDate": 1493858818000
        |}
      """
        .stripMargin
        .replace("--accountId--", accountId.toString)
        .replace("--summonerId--", Random.nextInt(10000).toString)
    }

    private def getDummySummonerForSummonerId(summonerId: Long): String = {
      """
        |{
        |    "profileIconId": 582,
        |    "name": SomeRandomSummoner,
        |    "summonerLevel": 30,
        |    "accountId": --accountId--,
        |    "id": --summonerId--,
        |    "revisionDate": 1493858818000
        |}
      """
        .stripMargin
        .replace("--accountId--", Random.nextInt(10000).toString)
        .replace("--summonerId--", summonerId.toString)
    }
  }

}
