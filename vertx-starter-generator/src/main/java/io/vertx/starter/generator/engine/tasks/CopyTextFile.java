package io.vertx.starter.generator.engine.tasks;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.ProjectGeneratorTask;
import io.vertx.starter.generator.io.FileProvider;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

import static java.util.Objects.requireNonNull;

public class CopyTextFile implements ProjectGeneratorTask {

  private final Logger log = LoggerFactory.getLogger(CopyTextFile.class);

  private final FileProvider fileProvider;
  private final FileSystem fileSystem;
  private final String source;
  private final String destination;

  public CopyTextFile(FileProvider fileProvider, FileSystem fileSystem, String source, String destination) {
    requireNonNull(fileProvider);
    requireNonNull(fileSystem);
    requireNonNull(source);
    requireNonNull(destination);
    this.fileProvider = fileProvider;
    this.fileSystem = fileSystem;
    this.source = source;
    this.destination = destination;
  }

  @Override
  public Future execute(Project project) {
    log.debug("Request to copy {} to {}", source, destination);
    requireNonNull(project);
    return fileProvider
      .resolve(source)
      .compose(path -> fileSystem.copy(path, project.getTargetDir().resolve(destination)));
  }
}
