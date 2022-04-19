package ru.tinkoff.backendacademy.petapp.db

import liquibase.{Contexts, Liquibase}
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import zio.{Task, _}
import zio.blocking.{Blocking, effectBlocking}
import zio.interop.catz._
import zio.interop.catz.implicits.rts

object MigrateDB extends zio.App {

  import doobie._

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = ZIO.effect {
    Transactor.fromDriverManager[Task](
      "org.sqlite.JDBC",
      "jdbc:sqlite:testdb.db",
      "user",
      ""
    )
  }.flatMap { xa =>
    run(args.head, xa)
  }.exitCode

  private def run(changeLogFile: String, xa: Transactor[Task]): ZIO[Blocking, Throwable, Unit] = {
    xa.connect(xa.kernel).toManagedZIO.use { connection =>
      effectBlocking {
        val database = DatabaseFactory.getInstance
          .findCorrectDatabaseImplementation(new JdbcConnection(connection))
        val classLoader = classOf[MigrateDB.type].getClassLoader
        val resourceAccessor = new ClassLoaderResourceAccessor(classLoader)
        val liquibase = new Liquibase(changeLogFile, resourceAccessor, database)
        liquibase.rollback(1, "")
        liquibase.update(new Contexts())
      }
    }
  }
}
