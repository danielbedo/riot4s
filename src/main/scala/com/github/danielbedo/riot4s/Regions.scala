package com.github.danielbedo.riot4s

object Regions {

  sealed abstract class Region(
                              name: String,
                              region: String,
                              platformId: String,
                              host: String,
                              isTrueRegion: Boolean
                              ) {
    def getHost() = host
  }

  case object BR extends Region("Brazil", "BR", "BR1", "br1.api.riotgames.com", false)
  case object EUNE extends Region("Eastern Europe", "EUNE", "EUN1", "eun1.api.riotgames.com", false)
  case object EUW extends Region("Western Europe", "EUW", "EUW1", "euw1.api.riotgames.com", false)
  case object JP extends Region("Japan", "JP", "JP1", "jp1.api.riotgames.com", false)
  case object KR extends Region("Korea", "KR", "KR", "kr.api.riotgames.com", false)
  case object LAN extends Region("Latin America North", "LAN", "LA1", "la1.api.riotgames.com", false)
  case object LAS extends Region("Latin America South", "LAS", "LA2", "la2.api.riotgames.com", false)
  case object NA extends Region("North America", "NA", "NA1", "na1.api.riotgames.com", false)
  case object OCE extends Region("Oceania", "OCE", "OC1", "oc1.api.riotgames.com", false)
  case object TR extends Region("Turkey", "TR", "TR1", "tr1.api.riotgames.com", false)
  case object RU extends Region("Russia", "RU", "RU", "ru.api.riotgames.com", false)
  case object PBE extends Region("PBE", "PBE", "PBE1", "pbe1.api.riotgames.com", false)
  case object GLOBAL extends Region("Global", "Global", "Global", "global.api.riotgames.com", true)

  val regions: Set[Region] = Set(BR, EUNE, EUW, JP, KR, LAN, LAS, NA, OCE, TR, RU, PBE, GLOBAL)
  val trueRegions: Set[Region] = Set(BR, EUNE, EUW, JP, KR, LAN, LAS, NA, OCE, TR, RU)
}
