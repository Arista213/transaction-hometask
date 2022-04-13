import sbt.addCompilerPlugin

name := "backend-academy"

version := "0.1"

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalamock" %% "scalamock" % "5.1.0"  % Test,
    "org.scalatest" %% "scalatest" % "3.2.10" % Test
  ),
  scalaVersion             := "2.13.8",
  coverageEnabled          := true,
  coverageFailOnMinimum    := true,
  coverageMinimumStmtTotal := 50,
  fork                     := true
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

val tapirVersion20 = "0.20.1"

lazy val backendSampleSettings = Seq(
  libraryDependencies ++= Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-zio1-http4s-server" % tapirVersion20,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % tapirVersion20,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion20,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui"         % tapirVersion20,
    "com.softwaremill.sttp.tapir" %% "tapir-zio1-http-server"   % tapirVersion20,
    "com.softwaremill.sttp.tapir" %% "tapir-zio1"               % tapirVersion20,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle"  % tapirVersion20,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe"         % tapirVersion20,
    "io.circe"                    %% "circe-core"               % circeVersion,
    "io.circe"                    %% "circe-generic"            % circeVersion,
    "io.circe"                    %% "circe-parser"             % circeVersion,
    "ch.qos.logback"               % "logback-classic"          % "1.2.11",
    "dev.zio"                     %% "zio-logging"              % "0.5.14",
    "dev.zio"                     %% "zio-interop-cats"         % "3.2.9.1",
    "com.h2database"               % "h2"                       % "2.1.210",
    "org.xerial"                   % "sqlite-jdbc"              % "3.28.0",
    "org.tpolecat"                %% "doobie-core"              % "1.0.0-RC2",
    "org.tpolecat"                %% "doobie-h2"                % "1.0.0-RC2",
    "io.getquill"                 %% "quill-jdbc-zio"           % "3.12.0",
    "org.liquibase"                % "liquibase-core"           % "3.6.1"
  ),
  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)
)

lazy val `backend-sample` =
  project in file("backend-sample") settings commonSettings ++ backendSampleSettings
