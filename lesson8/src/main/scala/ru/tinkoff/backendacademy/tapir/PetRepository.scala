package ru.tinkoff.backendacademy.tapir

import zio.{IO, UIO, URIO, ZIO}

trait PetRepository {
  def findPet(petId: Int): ZIO[Any, String, String]
}

class LoggingPetRepository(delegate: PetRepository) extends PetRepository {
  override def findPet(petId: Int): ZIO[Any, String, String] = for {
    _   <- URIO(println(s"Looking for pet $petId"))
    res <- delegate.findPet(petId)
    _   <- URIO(println(s"Found pet $res"))
  } yield res
}

object SinglePetRepository extends PetRepository {
  override def findPet(petId: Int): ZIO[Any, String, String] =
    if (petId == 35) {
      UIO("Tapirus terrestris")
    } else {
      IO.fail("Unknown pet id")
    }
}
