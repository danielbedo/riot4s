import Dependencies._


val circeVersion = "0.7.0"
val akkaVersion = "2.4.17"
val akkaHttpVersion = "10.0.5"
val catsVersion = "0.9.0"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.github.danielbedo",
      scalaVersion := "2.11.7",
      version      := "0.1.0-SNAPSHOT",
      test in assembly := {}
    )),
    name := "riot4s",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "org.typelevel" %% "cats" % catsVersion,
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "com.github.cb372" %% "scalacache-guava" % "0.9.3",
      "com.github.cb372" %% "scalacache-core" % "0.9.3",

      scalaTest % Test))


assemblyJarName in assembly := "riot4s.jar"