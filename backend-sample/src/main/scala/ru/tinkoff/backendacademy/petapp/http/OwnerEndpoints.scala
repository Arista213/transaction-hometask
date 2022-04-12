package ru.tinkoff.backendacademy.petapp.http

import ru.tinkoff.backendacademy.petapp.domain.{Owner, Pet}
import ru.tinkoff.backendacademy.petapp.repository.owner.OwnerRepository
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe._
import sttp.tapir.ztapir._

object OwnerEndpoints {
  def apply(
      ownerRepository: OwnerRepository
  ): List[ZServerEndpoint[Any, Any]] = {
    val ownerEndpoint: ZServerEndpoint[Any, Any] =
      endpoint.post
        .in("owner" / "pets" / "by-pet")
        .in(jsonBody[Pet])
        .errorOut(stringBody)
        .out(jsonBody[Seq[Pet]])
        .zServerLogic(ownerRepository.findAllPetsOfOwner)

    val noPetsOwnersEndpoint: ZServerEndpoint[Any, Any] = endpoint.get
      .in("owner" / "without-pets")
      .errorOut(stringBody)
      .out(jsonBody[Seq[Owner]])
      .zServerLogic(_ => ownerRepository.findOwnersWithoutPets())

    List(ownerEndpoint, noPetsOwnersEndpoint)
  }
}
