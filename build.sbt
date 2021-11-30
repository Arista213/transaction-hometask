import sbt.addCompilerPlugin

name := "backend-academy"

version := "0.1"

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalamock" %% "scalamock" % "5.1.0"  % Test,
    "org.scalatest" %% "scalatest" % "3.2.10" % Test
  ),
  scalaVersion := "2.13.6",
  coverageEnabled := true,
  coverageFailOnMinimum := true,
  coverageMinimumStmtTotal := 50
)

lazy val catsSettings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.3.0"
  )
)

val circeVersion      = "0.14.1"
val tapirVersion      = "0.19.0"
val zioLoggingVersion = "0.5.14"

lazy val zioTapirSettings = Seq(
  libraryDependencies ++= Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-zio-http4s-server"  % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui"         % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle"  % tapirVersion,
    "dev.zio"                     %% "zio-logging"              % zioLoggingVersion,
    "io.circe"                    %% "circe-core"               % circeVersion,
    "io.circe"                    %% "circe-generic"            % circeVersion,
    "io.circe"                    %% "circe-parser"             % circeVersion
//    "io.d11"                      %% "zhttp"                   % "v1.0.0.0-RC17"
  ),
  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)
)

lazy val root = project in file(".") aggregate (
  lesson3,
  lesson4
)

lazy val lesson3 = project in file("lesson3") settings commonSettings
lazy val lesson4 = (project in file("lesson4"))
  .settings(commonSettings)
  .settings(
    Compile / mainClass := Some("ru.tinkoff.backendacademy.wordstorage.ConsoleInMemory")
  )
  .enablePlugins(JavaAppPackaging, AshScriptPlugin, DockerPlugin)
lazy val lesson6  = project in file("lesson6") settings commonSettings ++ catsSettings
lazy val lesson8  = project in file("lesson8") settings commonSettings ++ zioTapirSettings
lazy val lesson9  = project in file("lesson9") settings commonSettings ++ catsSettings
lazy val lesson11 = project in file("lesson11") settings commonSettings
