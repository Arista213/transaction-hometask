package ru.tinkoff.backendacademy.petapp.http

import sttp.tapir.ztapir._
import zio.{Has, ZIO}
import sttp.tapir.server.ziohttp._
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import zhttp.service.Server

class HttpServer(
    endpoints: List[ZServerEndpoint[Any, Any]]
) {

  private val routes =
    ZioHttpInterpreter().toHttp(
      endpoints ++ SwaggerInterpreter().fromServerEndpoints(endpoints, "Our pets", "1.0")
    )

  val server: ZIO[Has[_], Throwable, Nothing] =
    Server.start(8080, routes)

}
