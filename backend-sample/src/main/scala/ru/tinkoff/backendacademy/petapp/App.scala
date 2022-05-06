package ru.tinkoff.backendacademy.petapp

import cats.effect.std.Dispatcher
import com.typesafe.config.ConfigFactory
import io.getquill.context.ZioJdbc
import io.getquill.{PostgresZioJdbcContext, SnakeCase}
import ru.tinkoff.backendacademy.petapp.http.{
  HttpServer,
  OwnerEndpoints,
  PetAndOwnerEndpoints,
  PetEndpoints
}
import ru.tinkoff.backendacademy.petapp.repository.owner.DatabaseOwnerRepository
import ru.tinkoff.backendacademy.petapp.repository.pet.{DatabasePetRepository, LoggingPetRepository}
import ru.tinkoff.backendacademy.petapp.repository.petandowner.DatabasePetOwnerRepository
import zio.{ExitCode, Has, Task, URIO, ZEnv, ZIO, ZLayer}

import javax.sql.DataSource
import scala.concurrent.Future

@scala.annotation.nowarn("cat=unused")
object App extends zio.App {

  val ctx = new PostgresZioJdbcContext[SnakeCase](SnakeCase)

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    val dataSource: ZLayer[Any, Throwable, Has[DataSource]] =
      ZioJdbc.DataSourceLayer.fromConfig(
        ConfigFactory.defaultApplication().resolve().getConfig("database")
      )

    ZIO
      .runtime[Any]
      .flatMap { implicit runtime =>
        (for {
          ds             <- dataSource
          consoleLogging <- zio.logging.Logging.console()
          databasePetRepository = new LoggingPetRepository(
            consoleLogging,
            new DatabasePetRepository(ds)
          )
          databaseOwnerRepository    = new DatabaseOwnerRepository(ds)
          databasePetOwnerRepository = new DatabasePetOwnerRepository(ds)
          endpoints = List(
            PetEndpoints(databasePetRepository),
            PetAndOwnerEndpoints(databasePetOwnerRepository)
          ) ++
            OwnerEndpoints(databaseOwnerRepository)
          server = new HttpServer(endpoints)
        } yield server).build.orDie.use { server =>
          server.server.exitCode
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
}
