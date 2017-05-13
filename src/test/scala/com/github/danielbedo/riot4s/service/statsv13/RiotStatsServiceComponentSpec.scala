package com.github.danielbedo.riot4s.service.statsv13

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

class RiotStatsServiceComponentSpec extends FlatSpec with Matchers with MockFactory with ApiTestUtils {

  class testFixture extends RiotStatsServiceComponent with LeagueApiComponent {
    val leagueApi = stub[LeagueApi]
    val unparseableApiResponse = EitherT.right[Future, ApiError, String](Future("{No one expects this}"))
    (leagueApi.get _).when(statsService.getUrlForSummaryBySummonerId(123, Regions.EUW), *).returns(unparseableApiResponse)

    val validResponse = getTestFile("/responses/stats/getSummary_valid.json")
    val validSummonerResponse = EitherT.right[Future, ApiError, String](Future(validResponse))
    (leagueApi.get _).when(statsService.getUrlForSummaryBySummonerId(123456, Regions.EUW), *).returns(validSummonerResponse)
  }

  "RiotStatsService" should "respond with unparsableJson if unknown response from API" in {
    val services = new testFixture
    val summonerResponse = services.statsService.getSummary(123, Regions.EUW).value
    val result = Await.result(summonerResponse, 1 seconds)

    assert(result == Left(ApiErrors.UnparseableJson))
  }

  "RiotStatsService" should "respond with decoded model class if correct API response received" in {
    val services = new testFixture
    val summonerResponse = services.statsService.getSummary(123456, Regions.EUW).value
    val result = Await.result(summonerResponse, 1 seconds)

    assert(result.isRight)
  }

}
