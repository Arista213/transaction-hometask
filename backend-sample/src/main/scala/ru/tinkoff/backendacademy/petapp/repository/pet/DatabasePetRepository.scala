package ru.tinkoff.backendacademy.petapp.repository.pet

import ru.tinkoff.backendacademy.petapp.domain.Pet
import zio.{Has, ZIO}

import java.sql.SQLException
import javax.sql.DataSource

class DatabasePetRepository(ds: Has[DataSource]) extends PetRepository {
  import ru.tinkoff.backendacademy.petapp.App.ctx._

  override def findPetName(petId: Int): ZIO[Any, String, String] = {

    val value: ZIO[Any, SQLException, List[String]] = run(
      petByName(petId)
    ).provide(ds)

    value
      .map(_.headOption)
      .flatMap(s => ZIO.fromOption(s))
      .mapError(e => s"Could not find pet: ${e}")
  }

  def petByName(petId: Int) = {
    quote {
      query[Pet]
        .filter(p => p.id == lift(petId))
        .map(_.name)
        .distinct
        .take(1)
    }
  }
}

// SELECT DISTINCT p.name FROM pet p WHERE p.id = $1 LIMIT 1
// SELECT DISTINCT p.name FROM pet p WHERE p.id = p.id; drop table pet; select 1 limit 1
