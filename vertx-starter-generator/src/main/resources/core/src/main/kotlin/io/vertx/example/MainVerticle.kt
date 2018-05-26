package io.vertx.example

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future

class MainVerticle : AbstractVerticle() {

    override fun start(startFuture: Future<Void>) {
        vertx
            .createHttpServer()
            .requestHandler { req ->
                req.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x!")
            }
            .listen(8080) { result ->
                if (result.succeeded()) {
                    startFuture.complete()
                } else {
                    startFuture.fail(result.cause());
                }
            }
        System.out.println("HTTP server started on port 8080")
    }
}
