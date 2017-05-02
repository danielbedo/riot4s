package com.danielbedo.riot4s.service.summoner


import cats.data.EitherT
import com.danielbedo.riot4s.Regions.Region
import com.danielbedo.riot4s.http.LeagueApiComponent
import com.danielbedo.riot4s.service.summoner.model.SummonerDTO
import io.circe.Error

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import io.circe.generic.auto._
import io.circe.parser._

trait SummonerServiceComponent {
  def summonerService: SummonerService

  trait SummonerService {
    def getSummonerByName(summonerName: String, region: Region): EitherT[Future, Error, SummonerDTO]
  }
}

trait DefaultSummonerServiceComponent extends SummonerServiceComponent {
  self: LeagueApiComponent =>

  def summonerService = new DefaultSummonerService

  class DefaultSummonerService extends SummonerService {

    def getSummonerByName(summonerName: String, region: Region): EitherT[Future, Error, SummonerDTO] = {
      val url = s"https://${region.getHost()}/lol/summoner/v3/summoners/by-name/$summonerName"
      val decoded = leagueApi
        .getAsString(url)
        .map { jsonString => decode[SummonerDTO](jsonString)}

      EitherT(decoded)
    }

  }

}
