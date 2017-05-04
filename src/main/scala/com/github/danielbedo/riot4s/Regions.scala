package com.github.danielbedo.riot4s

object Regions {

  sealed abstract class Region(
                              val name: String,
                              val region: String,
                              val platformId: String,
                              val host: String,
                              val isTrueRegion: Boolean
                              )

  case object BR extends Region("Brazil", "BR", "BR1", "https://br1.api.riotgames.com", false)
  case object EUNE extends Region("Eastern Europe", "EUNE", "EUN1", "https://eun1.api.riotgames.com", false)
  case object EUW extends Region("Western Europe", "EUW", "EUW1", "https://euw1.api.riotgames.com", false)
  case object JP extends Region("Japan", "JP", "JP1", "https://jp1.api.riotgames.com", false)
  case object KR extends Region("Korea", "KR", "KR", "https://kr.api.riotgames.com", false)
  case object LAN extends Region("Latin America North", "LAN", "LA1", "https://la1.api.riotgames.com", false)
  case object LAS extends Region("Latin America South", "LAS", "LA2", "https://la2.api.riotgames.com", false)
  case object NA extends Region("North America", "NA", "NA1", "https://na1.api.riotgames.com", false)
  case object OCE extends Region("Oceania", "OCE", "OC1", "https://oc1.api.riotgames.com", false)
  case object TR extends Region("Turkey", "TR", "TR1", "https://tr1.api.riotgames.com", false)
  case object RU extends Region("Russia", "RU", "RU", "https://ru.api.riotgames.com", false)
  case object PBE extends Region("PBE", "PBE", "PBE1", "https://pbe1.api.riotgames.com", false)
  case object GLOBAL extends Region("Global", "Global", "Global", "https://global.api.riotgames.com", true)

  val regions: Set[Region] = Set(BR, EUNE, EUW, JP, KR, LAN, LAS, NA, OCE, TR, RU, PBE, GLOBAL)
  val trueRegions: Set[Region] = Set(BR, EUNE, EUW, JP, KR, LAN, LAS, NA, OCE, TR, RU)
}
