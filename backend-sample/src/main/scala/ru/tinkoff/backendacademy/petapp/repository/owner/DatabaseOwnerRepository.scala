package ru.tinkoff.backendacademy.petapp.repository.owner

import ru.tinkoff.backendacademy.petapp.domain.{Owner, Pet}
import zio.{Has, ZIO}

import javax.sql.DataSource

class DatabaseOwnerRepository(ds: Has[DataSource]) extends OwnerRepository {
  import ru.tinkoff.backendacademy.petapp.App.ctx._

  override def findAllPetsOfOwner(pet: Pet): ZIO[Any, String, Seq[Pet]] = {
    pet.ownerId match {
      case Some(ownerId) =>
        run(
          quote(
            query[Pet].filter(_.ownerId.contains(lift(ownerId)))
          )
        )
          .mapError(_ => "Could not find pets")
          .provide(ds)
      case None => ZIO.succeed(Seq.empty[Pet])
    }
  }

  override def findOwnersWithoutPets(): ZIO[Any, String, Seq[Owner]] = {
    run(quote {
      query[Owner]
        .leftJoin(query[Pet])
        .on { case (o, p) => p.ownerId.contains(o.id) }
        .filter { case (_, maybePet) => maybePet.map(_.id).isEmpty }
        .distinct
    }).provide(ds)
      .mapError(_.getMessage)
      .map(_.map(_._1))
  }

  /*
  run(quote {
      query[Owner]
        .leftJoin(query[Pet])
        .on { case (o, p) => o.id == p.ownerId }
        .filter { case (_, maybePet) => !maybePet.isDefined }
        .map { case (owner, _) => owner }
    })
      .provide(ds)
      .mapError(e => e.getMessage)
   */
}
