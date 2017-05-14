package com.github.danielbedo.riot4s.ratelimit

import com.github.danielbedo.riot4s.Regions
import com.github.danielbedo.riot4s.Regions.Region
import org.joda.time.DateTime

import scala.collection.mutable

case class ApiCall(region: Region, time: DateTime)

trait RateLimiterComponent {
  val rateLimiter: RateLimiter

  trait RateLimiter {
    def getTotalCount(region: Region): Int
    def addCall(region: Region): Unit
  }
}

trait QueueRateLimiterComponent extends RateLimiterComponent {
  val rateLimiter = new QueueRateLimiter

  class QueueRateLimiter extends RateLimiter {
    val rateLimitMap: Map[Region, mutable.Queue[DateTime]] = Regions.regions.map { region =>
      region -> mutable.Queue[DateTime]()
    }.toMap

    def getTotalCount(region: Region): Int = rateLimitMap.getOrElse(region, mutable.Queue[DateTime]()).size
    def addCall(region: Region) = rateLimitMap.getOrElse(region, mutable.Queue[DateTime]()).enqueue(DateTime.now())

  }

}