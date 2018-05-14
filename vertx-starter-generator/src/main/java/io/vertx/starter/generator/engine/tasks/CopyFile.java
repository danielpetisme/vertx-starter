package io.vertx.starter.generator.engine.tasks;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.Task;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

import static java.util.Objects.requireNonNull;

public class CopyFile extends Task {

    private final Logger log = LoggerFactory.getLogger(CopyFile.class);

    private final FileSystem fileSystem;
    private String source;
    private String destination;

    public CopyFile(Project project, FileSystem fileSystem) {
        super(project);
        this.fileSystem = fileSystem;
    }

    public String source() {
        return source;
    }

    public CopyFile source(String source) {
        this.source = source;
        return this;
    }

    public String destination() {
        return destination;
    }

    public CopyFile destination(String destination) {
        this.destination = destination;
        return this;
    }

    @Override
    public Future execute() {
        log.debug("Request to copy source {} to destination {}", source, destination);
        requireNonNull(source);
        requireNonNull(destination);
        return fileSystem.copy(getProject().getProjectDir().resolve(source), getProject().getOutputDir().resolve(destination));
    }
}
