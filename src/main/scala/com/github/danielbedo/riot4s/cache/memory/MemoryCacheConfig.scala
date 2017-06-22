package com.github.danielbedo.riot4s.cache.memory

import scala.concurrent.duration._

case class MemoryCacheConfig(maximumSize: Long = 10000L, ttl: Option[Duration] = Some(3600.seconds))
