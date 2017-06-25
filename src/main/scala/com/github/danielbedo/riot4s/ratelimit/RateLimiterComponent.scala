package com.github.danielbedo.riot4s.ratelimit

import com.github.danielbedo.riot4s.Regions
import com.github.danielbedo.riot4s.Regions.Region
import org.joda.time.DateTime

import scala.collection.mutable

case class ApiCall(region: Region, time: DateTime)

sealed trait RateLimiter {
  def getTotalCount(region: Region): Int
  def addCall(region: Region): Unit
}

class NoopRateLimiter extends RateLimiter {

  override def getTotalCount(region: Region): Int = 0
  override def addCall(region: Region): Unit = ()
}

class QueueRateLimiter(regions: Set[Region]) extends RateLimiter {
  val rateLimitMap: Map[Region, mutable.Queue[DateTime]] = regions.map { region =>
    region -> mutable.Queue[DateTime]()
  }.toMap

  def getTotalCount(region: Region): Int = rateLimitMap.getOrElse(region, mutable.Queue[DateTime]()).size
  def addCall(region: Region): Unit = rateLimitMap.getOrElse(region, mutable.Queue[DateTime]()).enqueue(DateTime.now())

}
