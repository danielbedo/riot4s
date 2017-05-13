package com.github.danielbedo.riot4s.service.summoner

import cats.data.EitherT
import com.github.danielbedo.ApiErrors.ApiError
import com.github.danielbedo.riot4s.Regions
import com.github.danielbedo.riot4s.http.LeagueApiComponent
import org.scalatest._
import cats.implicits._
import com.github.danielbedo.ApiErrors
import com.github.danielbedo.riot4s.Regions.EUW

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalamock.scalatest.MockFactory
class RiotSummonerServiceComponentSpec extends FlatSpec with Matchers with MockFactory {

  class testFixture extends RiotSummonerServiceComponent with LeagueApiComponent {
    private def getUrlForSummonerName(name: String) = s"${EUW.host}/lol/summoner/v3/summoners/by-name/$name"

    val leagueApi = stub[LeagueApi]
    val unparseableApiResponse = EitherT.right[Future, ApiError, String](Future("{No one expects this}"))
    (leagueApi.get _).when(getUrlForSummonerName("failingSummoner"), *).returns(unparseableApiResponse)

    val validResponse =
      """
        |{
        |    "profileIconId": 582,
        |    "name": "validSummoner",
        |    "summonerLevel": 30,
        |    "accountId": 1234,
        |    "id": 1111,
        |    "revisionDate": 1494638483000
        |}
      """.stripMargin
    val validSummonerResponse = EitherT.right[Future, ApiError, String](Future(validResponse))
    (leagueApi.get _).when(getUrlForSummonerName("validSummoner"), *).returns(validSummonerResponse)
  }

  "RiotSummonerService" should "respond with unparsableJson if unknown response from API" in {
    val services = new testFixture
    val summonerResponse = services.summonerService.getSummonerByName("failingSummoner", Regions.EUW).value
    val result = Await.result(summonerResponse, 1 seconds)

    assert(result == Left(ApiErrors.UnparseableJson))
  }

  "RiotSummonerService" should "return a parsed Summoner object if json from API is valid" in {
    val services = new testFixture
    val summonerResponse = services.summonerService.getSummonerByName("validSummoner", Regions.EUW).value
    val result = Await.result(summonerResponse, 1 seconds)

    assert(result.isRight)
    val summoner = result.right.get
    assert(summoner.name == "validSummoner")
    assert(summoner.id == 1111)
  }

}
