package ru.tinkoff.backendacademy.tapir

import sttp.tapir.ztapir._

object PetServerEndpoint {
  def apply(
      petRepository: PetRepository
  ): ZServerEndpoint[Any, Any] = {
    val petEndpoint: RichZEndpoint[Unit, Int, String, String, Any] =
      endpoint.get
        .in("pet" / path[Int]("petId"))
        .errorOut(stringBody)
        .out(stringBody)

    petEndpoint.zServerLogic[Any](petRepository.findPet)
  }
}
