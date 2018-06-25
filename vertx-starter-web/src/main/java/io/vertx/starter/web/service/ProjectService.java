package io.vertx.starter.web.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.starter.web.repository.ProjectRepository;

public class ProjectService {

  private final EventBus eventBus;
  private final ProjectRepository projectRepository;

  public ProjectService(EventBus eventBus, ProjectRepository projectRepository) {
    this.eventBus = eventBus;
    this.projectRepository = projectRepository;
  }

  public void create(JsonObject request, Handler<AsyncResult<JsonObject>> reply) {
    eventBus.send("project.requested", request, ar -> {
      if (ar.succeeded()) {
        JsonObject project = (JsonObject) ar.result().body();
        projectRepository.save(project);
        reply.handle(Future.succeededFuture(project));
      } else {
        reply.handle(Future.failedFuture(ar.cause()));
      }

    });
  }

}
