package ru.tinkoff.backendacademy.petapp.repository.owner

import ru.tinkoff.backendacademy.petapp.domain.{Owner, Pet}
import zio.ZIO

trait OwnerRepository {
  def findAllPetsOfOwner(pet: Pet): ZIO[Any, String, Seq[Pet]]
  def findOwnersWithoutPets(): ZIO[Any, String, Seq[Owner]]
}
