package ru.tinkoff.backendacademy.petapp.http

import ru.tinkoff.backendacademy.petapp.repository.pet.PetRepository
import sttp.tapir.Endpoint
import sttp.tapir.ztapir._

object PetEndpoints {
  def apply(
      petRepository: PetRepository
  ): ZServerEndpoint[Any, Any] = {
    val petEndpoint: Endpoint[Unit, Int, String, String, Any] =
      endpoint.get
        .in("pet" / path[Int]("petId"))
        .errorOut(stringBody)
        .out(stringBody)

    petEndpoint.zServerLogic(petRepository.findPetName)
  }
}
