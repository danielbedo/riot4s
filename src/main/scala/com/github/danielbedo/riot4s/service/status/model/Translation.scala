package com.github.danielbedo.riot4s.service.status.model

case class ShardStatus(name: String, region_tag: String, hostname: String, services: List[Service], slug: String, locales: List[String])
case class Service(status: String, incidents: List[Incident], name: String, slug: String)
case class Incident(active: Boolean, created_at: String, id: Long, updates: List[Message])
case class Message(severity: String, author: String, created_at: String, translations: List[Translation], updated_at: String, content: String, id: String)
case class Translation(locale: String, content: String, updated_at: String)