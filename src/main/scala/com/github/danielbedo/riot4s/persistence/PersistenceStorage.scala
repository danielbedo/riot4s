package com.github.danielbedo.riot4s.persistence

import com.github.danielbedo.riot4s.builder.PersistenceConfig

trait PersistenceStorage {

}

object PersistenceStorage {

  def fromConfig(persistenceConfig: Option[PersistenceConfig]): PersistenceStorage = {
    persistenceConfig match {
      case Some(conf) => new EsPersistenceStorage(conf)
      case None => new NoopPersistenceStorage
    }
  }

}

class NoopPersistenceStorage extends PersistenceStorage
class EsPersistenceStorage(config: PersistenceConfig) extends PersistenceStorage
