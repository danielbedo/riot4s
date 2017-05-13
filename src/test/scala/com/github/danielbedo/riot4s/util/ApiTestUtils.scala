package com.github.danielbedo.riot4s.util


trait ApiTestUtils {
  def getTestFile(path: String) = {
    val res = getClass.getResourceAsStream(path)
    scala.io.Source.fromInputStream( res ).mkString
  }
}
