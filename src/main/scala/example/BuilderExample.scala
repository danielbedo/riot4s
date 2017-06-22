package example

import com.github.danielbedo.riot4s.builder.{ApiBuilder}

object BuilderExample {

  def main(args: Array[String]): Unit = {
    val api = ApiBuilder()
                .build()
  }

}
