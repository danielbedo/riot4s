package com.github.danielbedo.riot4s.service.status

import cats.data.EitherT
import com.github.danielbedo.ApiErrors.{ApiError, UnparseableJson}
import com.github.danielbedo.riot4s.Regions.Region
import com.github.danielbedo.riot4s.http.LeagueApiComponent
import com.github.danielbedo.riot4s.service.status.model.ShardStatus
import io.circe._
import io.circe.parser._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import io.circe.generic.auto._
import cats.implicits._


trait StatusServiceComponent {
  def statusService: StatusService

  trait StatusService {
    def getShardData(region: Region): EitherT[Future, ApiError, ShardStatus]
  }

}

trait DefaultStatusServiceComponent extends StatusServiceComponent {
  self: LeagueApiComponent =>

  def statusService = new DefaultStatusService

  class DefaultStatusService extends StatusService {

    def getShardData(region: Region): EitherT[Future, ApiError, ShardStatus] = {
      val url = s"${region.host}/lol/status/v3/shard-data"

      for {
        response <- leagueApi.get(url)
        summonerData <- EitherT.fromEither[Future](decode[ShardStatus](response)).leftMap{ err =>
          val newErr: ApiError = UnparseableJson
          newErr
        }
      } yield summonerData

    }

  }

}
