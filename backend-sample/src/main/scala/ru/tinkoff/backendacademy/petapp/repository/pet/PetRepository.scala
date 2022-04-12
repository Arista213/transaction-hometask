package ru.tinkoff.backendacademy.petapp.repository.pet

import zio.ZIO

// R => Either[E, A]
// ZIO[R, E, A]
// => Either[E, A]
// ZIO[Any, E, A]
trait PetRepository {
  def findPetName(petId: Int): ZIO[Any, String, String]
}
