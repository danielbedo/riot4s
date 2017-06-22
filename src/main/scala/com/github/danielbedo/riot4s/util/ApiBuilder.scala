package com.github.danielbedo.riot4s.util

import akka.actor.ActorSystem
import com.github.danielbedo.riot4s.ActorSystemProvider
import com.github.danielbedo.riot4s.http.DefaultLeagueApiComponent
import com.github.danielbedo.riot4s.service.statsv13.RiotStatsServiceComponent
import com.github.danielbedo.riot4s.service.status.RiotStatusServiceComponent
import com.github.danielbedo.riot4s.service.summoner.RiotSummonerServiceComponent

case class ApiBuilder(key: String)(implicit as: ActorSystem) {

  def build() = {
    new DefaultLeagueApiComponent
      with ActorSystemProvider
      with RiotStatusServiceComponent
      with RiotSummonerServiceComponent
      with RiotStatsServiceComponent {
      override val actorSystem = as
      override val apiKey: String = key
    }
  }

}