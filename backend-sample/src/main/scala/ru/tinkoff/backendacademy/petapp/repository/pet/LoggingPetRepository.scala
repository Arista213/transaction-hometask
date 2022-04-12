package ru.tinkoff.backendacademy.petapp.repository.pet

import zio.ZIO
import zio.logging.Logging

class LoggingPetRepository(logging: Logging, delegate: PetRepository) extends PetRepository {
  override def findPetName(petId: Int): ZIO[Any, String, String] = (
    for {
      _   <- Logging.info(s"Looking for pet $petId")
      res <- delegate.findPetName(petId)
      _   <- Logging.info(s"Found pet $res")
    } yield res
  ).provide(logging)
}
