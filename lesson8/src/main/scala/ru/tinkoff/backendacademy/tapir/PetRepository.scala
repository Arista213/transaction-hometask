package ru.tinkoff.backendacademy.tapir

import zio.logging.Logging
import zio.logging.log.info
import zio.{IO, UIO, ZIO}

trait PetRepository {
  def findPet(petId: Int): ZIO[Any, String, String]
}

class LoggingPetRepository(
    delegate: PetRepository,
    loggingEnv: Logging
) extends PetRepository {
  override def findPet(petId: Int): ZIO[Any, String, String] = (for {
    _   <- info(s"Looking for pet $petId")
    res <- delegate.findPet(petId)
    _   <- info(s"Found pet $res")
  } yield res).provide(loggingEnv)
}

object SinglePetRepository extends PetRepository {
  override def findPet(petId: Int): ZIO[Any, String, String] =
    if (petId == 35) {
      UIO("Tapirus terrestris")
    } else {
      IO.fail("Unknown pet id")
    }
}
