package com.github.danielbedo.riot4s

import akka.actor.ActorSystem
import akka.stream.Materializer

import scala.concurrent.ExecutionContext

trait ActorSystemProvider {
  implicit def actorSystem: ActorSystem
}
