package ru.tinkoff.backendacademy.petapp.http

import ru.tinkoff.backendacademy.petapp.domain.PetWithOwner
import ru.tinkoff.backendacademy.petapp.repository.petandowner.DatabasePetOwnerRepository
import sttp.tapir.Endpoint
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe
import sttp.tapir.ztapir._

object PetAndOwnerEndpoints {
  def apply(
      databasePetOwnerRepository: DatabasePetOwnerRepository
  ): ZServerEndpoint[Any, Any] = {
    val petEndpoint: Endpoint[Unit, PetWithOwner, String, Unit, Any] =
      endpoint.post
        .in("pets")
        .in(circe.jsonBody[PetWithOwner])
        .errorOut(stringBody)

    petEndpoint.zServerLogic(databasePetOwnerRepository.save)
  }
}
