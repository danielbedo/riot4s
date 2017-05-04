package com.github.danielbedo.riot4s.service.status

import cats.data.EitherT
import com.github.danielbedo.riot4s.Regions.Region
import com.github.danielbedo.riot4s.http.LeagueApiComponent
import com.github.danielbedo.riot4s.service.status.model.ShardStatus
import io.circe._
import io.circe.parser._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import io.circe.generic.auto._

trait StatusServiceComponent {
  def statusService: StatusService

  trait StatusService {
    def getShardData(region: Region): EitherT[Future, Error, ShardStatus]
  }

}

trait DefaultStatusServiceComponent extends StatusServiceComponent {
  self: LeagueApiComponent =>

  def statusService = new DefaultStatusService

  class DefaultStatusService extends StatusService {

    def getShardData(region: Region): EitherT[Future, Error, ShardStatus] = {
      val url = s"${region.host}/lol/status/v3/shard-data"
      val decoded = leagueApi.get(url)
      .map { jsonString => decode[ShardStatus](jsonString)}

      EitherT(decoded)
    }

  }

}
