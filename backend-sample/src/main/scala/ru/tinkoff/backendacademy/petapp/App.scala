package ru.tinkoff.backendacademy.petapp

import cats.effect.std.Dispatcher
import com.typesafe.config.ConfigFactory
import doobie.Transactor
import doobie.implicits.{toConnectionIOOps, toSqlInterpolator}
import io.getquill.context.ZioJdbc
import io.getquill.{H2ZioJdbcContext, SnakeCase}
import ru.tinkoff.backendacademy.petapp.http.{HttpServer, OwnerEndpoints, PetEndpoints}
import ru.tinkoff.backendacademy.petapp.repository.owner.DatabaseOwnerRepository
import ru.tinkoff.backendacademy.petapp.repository.pet.{DatabasePetRepository, LoggingPetRepository}
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
            "jdbcUrl"         -> "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            "username"        -> "user",
            "password"        -> "",
            "driverClassName" -> "org.h2.Driver"
          ).asJava
        )
      )

    ZIO
      .runtime[Any]
      .flatMap { implicit runtime =>
        val xa: Transactor[Task] =
          Transactor.fromDriverManager[Task](
            "org.h2.Driver",
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
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

  private def prepareDatabase(xa: Transactor[Task]): ZIO[Any, Throwable, Unit] = ZIO.collectAll_(
    Seq(
      sql"""create table owner(
           |  id bigint auto_increment primary key,
           |  name varchar not null
           |)""".stripMargin,
      sql"""create table pet(
           |  id bigint auto_increment primary key,
           |  name varchar not null,
           |  owner_id bigint,
           |  foreign key (owner_id) references owner(id)
           |)""".stripMargin,
      sql"""insert into owner(id, name) values 
           |(20, 'Mehmed Vahideddin'),
           |(2, 'Andrey Golikov'),
           |(1, 'Mr Fyodor')""".stripMargin,
      sql"""insert into pet(id, name, owner_id) values
           |(35, 'tapir', null),
           |(21, 'yak', 20),
           |(20, 'elephant', 20),
           |(1, 'cat', 1),
           |(2, 'dog', 1)""".stripMargin
    )
      .map(_.update.run.transact(xa))
  )
}
