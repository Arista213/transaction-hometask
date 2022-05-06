package ru.tinkoff.backendacademy.petapp.repository.pet

import ru.tinkoff.backendacademy.petapp.domain.Pet
import zio.{Has, ZIO}

import javax.sql.DataSource

class DatabasePetRepository(ds: Has[DataSource]) extends PetRepository {

  import ru.tinkoff.backendacademy.petapp.App.ctx
  import ru.tinkoff.backendacademy.petapp.App.ctx._

  case class PetFullName(petId: Long, fullName: String)

  override def findPetName(petId: Long): ZIO[Any, String, String] = {
    transaction(for {
      pets <- ctx.run(quote {
        query[Pet].filter(p => p.id == lift(petId))
      })
      maybeName = pets.headOption.map(_.name)
      name <- ZIO
        .fromOption(maybeName)
        .mapError(_ => new IllegalArgumentException("Could not find pet"))
//      _ <- ctx.run(quote(query[PetFullName].insert(PetFullName(lift(petId), lift("Mr. " + name)))))
      //      _ <- fullNameService.getFullName(name)
      fullName <- ZIO.cond(
        !name.startsWith("a"),
        "Mr. " + name,
        new IllegalArgumentException("Failed to get full name")
      )
    } yield fullName)
      .provide(ds)
      .mapError(e => s"Could not find pet: ${e}")
  }
}

// SELECT DISTINCT p.name FROM pet p WHERE p.id = $1 LIMIT 1
// SELECT DISTINCT p.name FROM pet p WHERE p.id = p.id; drop table pet; select 1 limit 1
