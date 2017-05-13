package com.github.danielbedo.riot4s.service.status

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import cats.implicits._
import com.github.danielbedo.riot4s.util.ApiTestUtils
import cats.data.EitherT
import com.github.danielbedo.ApiErrors
import com.github.danielbedo.ApiErrors.ApiError
import com.github.danielbedo.riot4s.Regions
import com.github.danielbedo.riot4s.http.LeagueApiComponent

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class RiotStatusServiceComponentSpec extends FlatSpec with Matchers with MockFactory with ApiTestUtils {

  trait testFixture extends RiotStatusServiceComponent with LeagueApiComponent {
    val leagueApi = stub[LeagueApi]
  }

  "RiotStatsService" should "respond with ShardData if correct API response arrives" in {
    val services = new testFixture {
      val validResponse = getTestFile("/responses/status/getShardData_valid.json")
      val validShardJsonResponse = EitherT.right[Future, ApiError, String](Future(validResponse))
      (leagueApi.get _).when(statusService.getUrlForShardData(Regions.EUW), *).returns(validShardJsonResponse)
    }

    val shardFuture = services.statusService.getShardData(Regions.EUW).value
    val shardResponse = Await.result(shardFuture, 1 seconds)

    assert(shardResponse.isRight)
  }

}
