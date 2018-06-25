package io.vertx.starter.service.handler;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.service.model.Project;
import io.vertx.starter.service.service.ProjectGeneratorService;

public class ProjectHandler {

  private final Logger log = LoggerFactory.getLogger(ProjectHandler.class);

  private final ProjectGeneratorService projectGeneratorService;

  public ProjectHandler(ProjectGeneratorService projectGeneratorService) {
    this.projectGeneratorService = projectGeneratorService;
  }

  public void onProjectRequested(Message<JsonObject> message) {
    Project project = message.body().mapTo(Project.class);
    projectGeneratorService.buildProject(project).setHandler(ar -> {
      if (ar.succeeded()) {
        message.reply(ar.result());
      } else {
        message.fail(500, ar.cause().getMessage());
      }
    });
  }

  public void onProjectCreated(Message<JsonObject> message) {

  }

}
