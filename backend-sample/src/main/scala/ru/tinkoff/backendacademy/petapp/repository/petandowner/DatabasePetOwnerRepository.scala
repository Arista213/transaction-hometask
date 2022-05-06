package ru.tinkoff.backendacademy.petapp.repository.petandowner

import ru.tinkoff.backendacademy.petapp.domain.{PetWithOwner, Owner, Pet}
import zio.{Has, ZIO}

import javax.sql.DataSource

case class DatabasePetOwnerRepository(ds: Has[DataSource]) extends PetOwnerRepository {
  import ru.tinkoff.backendacademy.petapp.App.ctx
  import ru.tinkoff.backendacademy.petapp.App.ctx._

  override def save(myType: PetWithOwner): ZIO[Any, String, Unit] = {
    transaction(for {
      _ <- ctx.run(quote {
        query[Owner].insert(lift(myType.owner))
      })
      _ <- ctx.run(quote {
        query[Pet].insert(Pet(lift(myType.id), lift(myType.name), lift(Option(myType.owner.id))))
      })
    } yield ())
      .provide(ds)
      .mapError(_ => "Could not make transaction")
  }
}
