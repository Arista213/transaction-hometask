package ru.tinkoff.backendacademy.petapp.db

import liquibase.{Contexts, Liquibase}
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import zio.{Task, _}
import zio.blocking.{Blocking, effectBlocking}
import zio.interop.catz._

object MigrateDB {
  import doobie._
  def run(changeLogFile: String, xa: Transactor[Task]): ZIO[Blocking, Throwable, Unit] = {
    xa.connect(xa.kernel).toManagedZIO.use { connection =>
      effectBlocking {
        val database = DatabaseFactory.getInstance
          .findCorrectDatabaseImplementation(new JdbcConnection(connection))
        val classLoader      = classOf[MigrateDB.type].getClassLoader
        val resourceAccessor = new ClassLoaderResourceAccessor(classLoader)
        val liquibase        = new Liquibase(changeLogFile, resourceAccessor, database)
        liquibase.rollback(1, "")
        liquibase.update(new Contexts())
      }
    }
  }
}
