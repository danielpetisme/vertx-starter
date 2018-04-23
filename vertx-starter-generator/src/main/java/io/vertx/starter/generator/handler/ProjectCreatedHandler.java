package io.vertx.starter.generator.handler;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.starter.generator.io.FutureFileSystem;

public class ProjectCreatedHandler {

  private final FutureFileSystem fileSystem;

  public ProjectCreatedHandler(FutureFileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  public void handle(Message<String> message) {
    String rootDir = message.body();
    fileSystem.deleteRecursive(rootDir);
//    String metadata = message.body();
//    String rootDir = metadata.getString("rootDir");
//    vertx.fileSystem().deleteRecursive(rootDir, true, ar -> {
//      if (ar.failed()) {
//        log.error("Impossible to delete temp directory {}: {}", rootDir, ar.cause().getMessage());
//      } else {
//        log.debug("Temp directory {} deleted", rootDir);
//      }
//    });
  }
}
