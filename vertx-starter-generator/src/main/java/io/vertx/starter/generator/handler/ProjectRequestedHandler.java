package io.vertx.starter.generator.handler;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.starter.generator.domain.Project;
import io.vertx.starter.generator.service.ArchiveService;
import io.vertx.starter.generator.service.ProjectGeneratorService;

import java.util.UUID;

public class ProjectRequestedHandler {

  private final String rootDir;
  private final ProjectGeneratorService projectGeneratorService;
  private final ArchiveService archiveService;

  public ProjectRequestedHandler(String rootDir, ProjectGeneratorService projectGeneratorService, ArchiveService archiveService) {
    this.rootDir = rootDir;
    this.projectGeneratorService = projectGeneratorService;
    this.archiveService = archiveService;
  }

  public void handle(Message<JsonObject> message) {
    Project project = message.body().mapTo(Project.class);
    String baseDir = rootDir + "/" + UUID.randomUUID().toString();
    projectGeneratorService
      .generate(baseDir, project);
//      .compose(it -> archiveService.archive(baseDir, project.getFormat()));
  }
}
