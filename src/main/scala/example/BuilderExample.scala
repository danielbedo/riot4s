package example

import com.github.danielbedo.riot4s.Regions
import com.github.danielbedo.riot4s.builder.ApiBuilder

object BuilderExample {

  def main(args: Array[String]): Unit = {
    val api = ApiBuilder("some-api-key")
      .withMemoryCache(100L)
      .withRateLimiter(Regions.EUW)
      .build
  }

}
