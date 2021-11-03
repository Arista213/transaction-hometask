package ru.tinkoff.backendacademy.tapir

import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.ztapir._

object PetServerEndpoint {
  def apply(
      petRepository: PetRepository
  ): ZServerEndpoint[Any, Int, String, String, ZioStreams with WebSockets] = {
    val petEndpoint =
      endpoint.get
        .in("pet" / path[Int]("petId"))
        .errorOut(stringBody)
        .out(stringBody)

    petEndpoint.zServerLogic(petRepository.findPet)
  }
}
