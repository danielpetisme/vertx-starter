package io.vertx.starter.generator.engine.tasks;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.SourceType;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

import java.nio.file.Paths;

import static io.vertx.starter.generator.util.ProjectUtil.path;
import static java.util.Objects.requireNonNull;

public class CopyDirectory extends CopyProjectFiles {

    private final Logger log = LoggerFactory.getLogger(CopyFile.class);

    public CopyDirectory(FileSystem fileSystem, String modelDir, SourceType sourceType) {
        super(fileSystem, modelDir, sourceType);
  }

  @Override
  public Future execute(Project project) {
    log.debug("Request to copy {} resources directory", sourceType);
    requireNonNull(project);
    String resourcesDir = resourcesDir();
      return fileSystem.copyDir(Paths.get(path(modelDir(project), resourcesDir)), project.getOutputDir().resolve(resourcesDir));
  }
}
