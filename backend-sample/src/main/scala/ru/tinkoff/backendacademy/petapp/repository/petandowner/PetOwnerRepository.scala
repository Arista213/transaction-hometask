package ru.tinkoff.backendacademy.petapp.repository.petandowner

import ru.tinkoff.backendacademy.petapp.domain.PetWithOwner
import zio.ZIO

trait PetOwnerRepository {
  def save(myType: PetWithOwner): ZIO[Any, String, Unit];
}
