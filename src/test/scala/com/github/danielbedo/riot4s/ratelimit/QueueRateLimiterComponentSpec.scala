package com.github.danielbedo.riot4s.ratelimit

import com.github.danielbedo.riot4s.Regions
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class QueueRateLimiterComponentSpec extends FlatSpec with Matchers {

  "The RateLimiter" should "return the correct size after element is inserted" in {
    val rateLimiterComponent = new QueueRateLimiterComponent {}
    val initial = rateLimiterComponent.rateLimiter.getTotalCount(Regions.EUW)
    assert(initial == 0)
    rateLimiterComponent.rateLimiter.addCall(Regions.EUW)
    rateLimiterComponent.rateLimiter.addCall(Regions.EUW)
    rateLimiterComponent.rateLimiter.addCall(Regions.EUNE)
    assert(rateLimiterComponent.rateLimiter.getTotalCount(Regions.EUW) == 2)
    assert(rateLimiterComponent.rateLimiter.getTotalCount(Regions.EUNE) == 1)

  }

}
