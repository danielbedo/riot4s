package com.danielbedo.riot4s.service.status

import cats.data.EitherT
import com.danielbedo.riot4s.Regions.Region
import com.danielbedo.riot4s.http.LeagueApiComponent
import com.danielbedo.riot4s.service.status.model.ShardStatus
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
      val url = "https://euw1.api.riotgames.com/lol/status/v3/shard-data"
      val decoded = leagueApi.getAsString(url)
      .map { jsonString => decode[ShardStatus](jsonString)}

      EitherT(decoded)
    }

  }

}
