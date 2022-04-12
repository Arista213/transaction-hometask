package ru.tinkoff.backendacademy.petapp.domain

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Owner(id: Long, name: String)

object Owner {
  implicit val ownerDecoder: Decoder[Owner] = deriveDecoder[Owner]
  implicit val ownerEncoder: Encoder[Owner] = deriveEncoder[Owner]
}
