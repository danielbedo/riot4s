package com.github.danielbedo.riot4s.ratelimit

import com.github.danielbedo.riot4s.Regions
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class QueueRateLimiterComponentSpec extends FlatSpec with Matchers {

  "The RateLimiter" should "return the correct size after element is inserted" in {
    val rateLimiter = new QueueRateLimiter(Regions.regions)
    val initial = rateLimiter.getTotalCount(Regions.EUW)
    assert(initial == 0)
    rateLimiter.addCall(Regions.EUW)
    rateLimiter.addCall(Regions.EUW)
    rateLimiter.addCall(Regions.EUNE)
    assert(rateLimiter.getTotalCount(Regions.EUW) == 2)
    assert(rateLimiter.getTotalCount(Regions.EUNE) == 1)
  }

}
