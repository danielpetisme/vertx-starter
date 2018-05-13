package io.vertx.starter.generator.engine.tasks;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.ProjectGeneratorTask;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

public class CopyFile implements ProjectGeneratorTask {

    private final Logger log = LoggerFactory.getLogger(CopyFile.class);

    private final FileSystem fileSystem;
    private final String source;
    private final String destination;

    public CopyFile(FileSystem fileSystem, String source, String destination) {
        requireNonNull(fileSystem);
        requireNonNull(source);
        requireNonNull(destination);
        this.fileSystem = fileSystem;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public Future execute(Project project) {
        log.debug("Request to copy {} to {}", source, destination);
        requireNonNull(project);
        return fileSystem.copy(Paths.get(source), project.getOutputDir().resolve(destination));
    }

}
