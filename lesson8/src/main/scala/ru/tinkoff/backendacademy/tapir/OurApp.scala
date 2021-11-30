package ru.tinkoff.backendacademy.tapir

import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.Console
import zio.logging.{LogFormat, LogLevel, Logging}
import zio.{ExitCode, URIO, ZEnv, ZIO, ZLayer}

object OurApp extends zio.App {

  private val logging: ZLayer[Console with Clock, Nothing, Logging] =
    Logging.console(
      logLevel = LogLevel.Info,
      format = LogFormat.ColoredLogFormat()
    )

  val programEnv: ZLayer[ZEnv, Nothing, ZEnv with Logging] = ZLayer.requires +!+ logging

  val program: ZIO[Logging with Clock with Blocking, Throwable, Unit] = for {
    petRepository <- ZIO.access((env: Logging) => new LoggingPetRepository(SinglePetRepository, env))
    endpoints = List(PetServerEndpoint(petRepository))
    server    = new ZioExampleHttp4sServer(endpoints)
    serve <- server.serve
  } yield serve

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] = {
    program.provideLayer(programEnv).exitCode
  }
}
