package ru.tinkoff.backendacademy.tapir

import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.swagger.SwaggerUI
import sttp.tapir.ztapir._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.interop.catz._
import zio.{RIO, ZEnv, ZIO}

class ZioExampleHttp4sServer(
    endpoints: List[ZServerEndpoint[Any, Int, String, String, ZioStreams with WebSockets]]
) {

  val routes =
    ZHttp4sServerInterpreter[Any]()
      .from(endpoints)
      .toRoutes

  val yaml: String = {
    import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
    import sttp.tapir.openapi.circe.yaml._
    OpenAPIDocsInterpreter()
      .serverEndpointsToOpenAPI(endpoints, "Our API", "1.0")
      .toYaml
  }

  val swaggerRoutes =
    ZHttp4sServerInterpreter()
      .from(SwaggerUI[RIO[Clock with Blocking, *]](yaml))
      .toRoutes

  val serve =
    ZIO.runtime[ZEnv].flatMap { implicit runtime =>
      BlazeServerBuilder[RIO[Clock with Blocking, *]](runtime.platform.executor.asEC)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(Router("/our-app" -> (routes)).orNotFound)
        .serve
        .compile
        .drain
    }

}
