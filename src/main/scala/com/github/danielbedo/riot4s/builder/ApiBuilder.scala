package com.github.danielbedo.riot4s.builder

import com.github.danielbedo.riot4s.persistence.PersistenceStorage

private class Api(persistenceStorage: PersistenceStorage)

case class ApiBuilder() {


  def build(): Api = {
    new Api(

    )
  }

}
