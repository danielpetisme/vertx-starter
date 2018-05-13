package io.vertx.starter.generator.handler;

import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.model.Project;
import io.vertx.starter.generator.service.ArchiveService;
import io.vertx.starter.generator.service.ProjectGeneratorService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class ProjectRequestedHandler {

    private final Logger log = LoggerFactory.getLogger(ProjectRequestedHandler.class);

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
        Path baseDir = Paths.get(rootDir).resolve(UUID.randomUUID().toString());
        if (project.getModel() == null || project.getModel().isEmpty()) {
            project.setModel("core");
        }
        project.setBaseDir(baseDir);
        project.setProjectDir();
        project.setOutputDir(baseDir.resolve(project.getArtifactId()));

        projectGeneratorService
            .generate(project)
            .compose(it -> archiveService.archive(project))
            .compose(archive -> {
                project.setArchivePath(((Path) archive).toAbsolutePath().toString());
                message.reply(JsonObject.mapFrom(project));
                return Future.succeededFuture();
            })
            .otherwise(error -> {
                log.error("Impossible to generate project: {} because {}", project, error);
                return Future.failedFuture("Impossible to generate project");
            });
    }

}
