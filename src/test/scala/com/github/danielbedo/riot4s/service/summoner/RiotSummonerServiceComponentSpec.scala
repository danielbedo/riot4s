package com.github.danielbedo.riot4s.service.summoner

import cats.data.EitherT
import com.github.danielbedo.ApiErrors.ApiError
import com.github.danielbedo.riot4s.Regions
import com.github.danielbedo.riot4s.http.LeagueApiComponent
import org.scalatest._
import cats.implicits._
import com.github.danielbedo.ApiErrors
import com.github.danielbedo.riot4s.util.ApiTestUtils

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalamock.scalatest.MockFactory

class RiotSummonerServiceComponentSpec extends FlatSpec with Matchers with MockFactory with ApiTestUtils {

  class testFixture extends RiotSummonerServiceComponent with LeagueApiComponent {
    val leagueApi = stub[LeagueApi]
    val unparseableApiResponse = EitherT.right[Future, ApiError, String](Future("{No one expects this}"))
    (leagueApi.get _).when(summonerService.getUrlForSummonerName("failingSummoner", Regions.EUW), *).returns(unparseableApiResponse)

    val validResponse = getTestFile("/responses/summoner/getSummonerByName_valid.json")
    val validSummonerResponse = EitherT.right[Future, ApiError, String](Future(validResponse))
    (leagueApi.get _).when(summonerService.getUrlForSummonerName("validSummoner", Regions.EUW), *).returns(validSummonerResponse)
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
    result map { s =>
      assert(s.name == "validSummoner")
      assert(s.accountId == 1234)
      assert(s.id == 1111)
      assert(s.profileIconId == 582)
      assert(s.revisionDate == 1494638483000l)
      assert(s.summonerLevel == 30)
    }
  }

}
