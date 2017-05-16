package example

import com.github.danielbedo.riot4s.builder.{ApiBuilder, PersistenceConfig}

object BuilderExample {

  def main(args: Array[String]): Unit = {
    val api = ApiBuilder()
                .withPersistenceStorage(new PersistenceConfig)
                .build()
  }

}
