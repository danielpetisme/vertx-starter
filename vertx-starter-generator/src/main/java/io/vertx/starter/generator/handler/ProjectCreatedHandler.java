package io.vertx.starter.generator.handler;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.model.Project;
import io.vertx.starter.generator.io.FileSystem;

public class ProjectCreatedHandler {

  private final Logger log = LoggerFactory.getLogger(ProjectCreatedHandler.class);

  private final FileSystem fileSystem;

  public ProjectCreatedHandler(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  public void handle(Message<JsonObject> message) {
    Project project = message.body().mapTo(Project.class);
    log.debug("Cleaning project {}", project);
    fileSystem
      .deleteRecursive(project.getBaseDir());
  }
}
