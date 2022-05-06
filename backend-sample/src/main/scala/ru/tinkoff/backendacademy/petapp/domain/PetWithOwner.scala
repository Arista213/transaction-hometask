package ru.tinkoff.backendacademy.petapp.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class PetWithOwner(id: Long, name: String, owner: Owner)

object PetWithOwner {
  implicit val petWithOwnerDecoder: Decoder[PetWithOwner] = deriveDecoder[PetWithOwner]
  implicit val petWithOwnerEncoder: Encoder[PetWithOwner] = deriveEncoder[PetWithOwner]
}
