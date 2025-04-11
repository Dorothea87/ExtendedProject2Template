name := """githubTutorial"""
organization := "com.example"

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "githubTutorial"
  )
  .enablePlugins(PlayScala)

resolvers += "HMRC-open-artefacts-maven2" at "https://open.artefacts.tax.service.gov.uk/maven2"
libraryDependencies ++= Seq(
  "uk.gov.hmrc.mongo"      %% "hmrc-mongo-play-28"   % "0.63.0",
  guice,
  "org.scalatest"          %% "scalatest"               % "3.2.19"             % Test,
  "org.scalamock"          %% "scalamock"               % "7.3.0"             % Test,
  "org.scalatestplus.play" %% "scalatestplus-play"   % "7.0.1"          % Test,
  ws,
  "org.typelevel"                %% "cats-core"                 % "2.13.0"
)


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
