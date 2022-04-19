package ru.tinkoff.backendacademy.petapp

import cats.effect.std.Dispatcher
import com.typesafe.config.ConfigFactory
import doobie.Transactor
import io.getquill.context.ZioJdbc
import io.getquill.{H2ZioJdbcContext, SnakeCase}
import ru.tinkoff.backendacademy.petapp.db.MigrateDB
import ru.tinkoff.backendacademy.petapp.http.{HttpServer, OwnerEndpoints, PetEndpoints}
import ru.tinkoff.backendacademy.petapp.repository.owner.DatabaseOwnerRepository
import ru.tinkoff.backendacademy.petapp.repository.pet.{DatabasePetRepository, LoggingPetRepository}
import zio.blocking.Blocking
import zio.interop.catz.asyncRuntimeInstance
import zio.interop.catz.implicits.rts
import zio.{ExitCode, Has, Task, URIO, ZEnv, ZIO, ZLayer}

import javax.sql.DataSource
import scala.concurrent.Future
import scala.jdk.CollectionConverters.MapHasAsJava

@scala.annotation.nowarn("cat=unused")
object App extends zio.App {

  val ctx = new H2ZioJdbcContext[SnakeCase](SnakeCase)

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    val dataSource: ZLayer[Any, Throwable, Has[DataSource]] =
      ZioJdbc.DataSourceLayer.fromConfig(
        ConfigFactory.parseMap(
          Map(
            "jdbcUrl"         -> "jdbc:sqlite:testdb.db",
            "username"        -> "user",
            "password"        -> "",
            "driverClassName" -> "org.sqlite.JDBC"
          ).asJava
        )
      )

    ZIO
      .runtime[Any]
      .flatMap { implicit runtime =>
        val xa: Transactor[Task] =
          Transactor.fromDriverManager[Task](
            "org.sqlite.JDBC",
            "jdbc:sqlite:testdb.db",
            "user",
            ""
          )

        (for {
          ds             <- dataSource
          consoleLogging <- zio.logging.Logging.console()
          databasePetRepository = new LoggingPetRepository(
            consoleLogging,
            new DatabasePetRepository(ds)
          )
          databaseOwnerRepository = new DatabaseOwnerRepository(ds)
          endpoints = List(
            PetEndpoints(databasePetRepository)
          ) ++ OwnerEndpoints(databaseOwnerRepository)
          server = new HttpServer(endpoints)
        } yield server).build.orDie.use { server =>
          (prepareDatabase(xa) *> server.server).exitCode
        }
      }
  }

  private implicit def dispatcher(implicit runtime: zio.Runtime[Any]): Dispatcher[Task] = {
    new Dispatcher[Task] {
      override def unsafeToFutureCancelable[A](fa: Task[A]): (Future[A], () => Future[Unit]) = {
        val value = unsafeRunToFuture(fa)
        (value.future, () => value.cancel().map(_ => ())(runtime.platform.executor.asEC))
      }
    }
  }

  private def prepareDatabase(xa: Transactor[Task]): ZIO[Blocking, Throwable, Unit] = {
    MigrateDB.run("migration.sql", xa)
  }
}
