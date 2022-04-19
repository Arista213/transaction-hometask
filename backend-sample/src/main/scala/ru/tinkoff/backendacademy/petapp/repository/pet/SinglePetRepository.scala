package ru.tinkoff.backendacademy.petapp.repository.pet

import zio.{IO, ZIO}

object SinglePetRepository extends PetRepository {
  override def findPetName(petId: Long): ZIO[Any, String, String] =
    if (petId == 35) {
      ZIO.succeed("Tapirus terrestris")
    } else {
      IO.fail("Unknown pet id")
    }
}
