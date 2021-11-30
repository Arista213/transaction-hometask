package ru.tinkoff.backendacademy.tapir

import cats.syntax.all._
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.interop.catz._
import zio.{RIO, ZEnv, ZIO}

class ZioExampleHttp4sServer(
    endpoints: List[ZServerEndpoint[Any, Any]]
) {

  val routes: HttpRoutes[RIO[Any with Clock with Blocking, *]] =
    ZHttp4sServerInterpreter[Any]()
      .from(endpoints)
      .toRoutes

  val swaggerRoutes: HttpRoutes[RIO[Any with Clock with Blocking, *]] =
    ZHttp4sServerInterpreter()
      .from(SwaggerInterpreter().fromServerEndpoints(endpoints, "Our pets", "1.0"))
      .toRoutes

  val serve =
    ZIO.runtime[ZEnv].flatMap { implicit runtime =>
      BlazeServerBuilder[RIO[Clock with Blocking, *]]
        .withExecutionContext(runtime.platform.executor.asEC)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(Router("/" -> (routes <+> swaggerRoutes)).orNotFound)
        .serve
        .compile
        .drain
    }

}
