[![Build Status](https://travis-ci.org/danielbedo/riot4s.svg?branch=master)](https://travis-ci.org/danielbedo/riot4s)

## riot4s - Riot Games API Wrapper for Scala

A Scala wrapper around the official League of Legends REST API.
(http://developer.riotgames.com/)
## Motivation
There are a countless number of libraries available for the League of Legends Api but we wanted one that uses idiomatic scala to 
access it. We use the Cake pattern to modularize the api, let you decide which endpoints you need, what caching layer you want etc.
We use cats and monad transformers to keep your code consise and easy to maintain. 
So far we only support a small number of endpoints, but its growing :)
## Code Example

```scala
object ApiTest extends App {

  // initialize the API with the Components you need  
  val services = new DefaultLeagueApiComponent
    with ActorSystemProvider
    with DefaultStatusServiceComponent
    with DefaultSummonerServiceComponent
    with DefaultStatsServiceComponent
    with GuavaServiceCacheComponent   // adds in-memory cache for all the api-calls
  {
    override val actorSystem = ActorSystem()
    override val apiKey: String = "your-api-key"
  }

  val statsFuture = for {
    summoner <- services.summonerService.getSummonerByName("username", Regions.EUW)
    stats <- services.statsService.getSummary(summoner.id, Regions.EUW)
  } yield stats
}
```
