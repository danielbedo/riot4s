[![Build Status](https://travis-ci.org/danielbedo/riot4s.svg?branch=master)](https://travis-ci.org/danielbedo/riot4s)

## riot4s - Riot Games API Wrapper for Scala

A Scala wrapper around the official League of Legends REST API.
(http://developer.riotgames.com/)
## Motivation
There are a countless number of libraries available for the League of Legends Api but we wanted one that uses idiomatic scala to 
access it. We use the Cake pattern to modularize the api, let you decide which endpoints you need, what caching layer you want etc.
We use cats and monad transformers to keep your code consise and easy to maintain. 
So far we only support a small number of endpoints, but its growing :)

## Quick Start 
### 1a, Creating the api the simple way
If you just want to access the API provided by Riot and want to take care about caching responses, persisting returned entities,
then you can construct the API in a really simple way:
```scala
val key = "your-api-key"
implicit val actorSystem = ActorSystem()
val api = ApiBuilder(key).build()
```
### 1b, Creating the api manually
If you want to specify which endpoints you need, what caching mechanism you want, what persistance layer you for a specific endpoint
you want, then you should create the api object yourself
```scala
  // initialize the API with the Components you need  
val api = new DefaultLeagueApiComponent
  with ActorSystemProvider
  with RiotStatusServiceComponent   // statusEndpoint
  with RiotSummonerServiceComponent // summonerEndpoint
  with RiotStatsServiceComponent    // statsEndpoint
  with GuavaServiceCacheComponent   // adds in-memory cache for all the api-calls
{
  override val actorSystem = ActorSystem()
  override val apiKey: String = "your-api-key"
}
```

### 2, Using the created api
```scala
val statsFuture = for {
  summoner <- api.summonerService.getSummonerByName("username", Regions.EUW)
  stats <- api.statsService.getSummary(summoner.id, Regions.EUW)
} yield stats

val result: Either[ApiError, PlayerStatsSummaryListDto] = Await.result(statsFuture.value,Duration.Inf)
```

## Implemented endpoints
|Riot Service   | Endpoint  | Version  |  Implemented | Component in the API  |
|---|---|---|---|---|
| Status    | status for a given shard         | V3   | Yes | RiotStatusServiceComponent     |
| Summoner  |  summoner by account id      | V3   | Yes  | RiotSummonerServiceComponent  |
| Summoner  |  summoner by summoner name   | V3   | Yes  | RiotSummonerServiceComponent  |
| Summoner  |  summoner by summoner id     | V3   | Yes  | RiotSummonerServiceComponent  |
| Stats     | player stats summaries by summoner id  | V1.3  | Partially  | RiotStatsServiceComponent  |
| Stats     | ranked stats summaries by summoner id  | V1.3  | No  |   |

## Persistence
Currently there is only support for Elasticsearch as persistence storage planned. If you need other storage systems please let us know.
