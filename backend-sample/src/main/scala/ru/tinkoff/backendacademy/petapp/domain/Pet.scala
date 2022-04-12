package ru.tinkoff.backendacademy.petapp.domain

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Pet(id: Long, name: String, ownerId: Option[Long])

object Pet {
  implicit val petDecoder: Decoder[Pet] = deriveDecoder[Pet]
  implicit val petEncoder: Encoder[Pet] = deriveEncoder[Pet]
}
