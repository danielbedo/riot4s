package com.github.danielbedo.riot4s.builder

import com.github.danielbedo.riot4s.persistence.PersistenceStorage

case class PersistenceConfig()

private class Api(persistenceStorage: PersistenceStorage)

case class ApiBuilder(persistenceStorage: Option[PersistenceConfig] = None) {

  def withPersistenceStorage(config: PersistenceConfig): ApiBuilder = this.copy(persistenceStorage = Some(config))

  def build(): Api = {
    new Api(
      persistenceStorage = PersistenceStorage.fromConfig(persistenceStorage)
    )
  }

}
