package ru.tinkoff.backendacademy.tapir

import zio.{ExitCode, URIO}

object OurApp extends zio.App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val petRepository = new LoggingPetRepository(SinglePetRepository)
    val endpoints     = List(PetServerEndpoint(petRepository))
    val server        = new ZioExampleHttp4sServer(endpoints)
    server.serve.exitCode
  }
}
