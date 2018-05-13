package io.vertx.starter.generator.engine.tasks;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.ProjectGeneratorTask;
import io.vertx.starter.generator.engine.SourceType;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

import static io.vertx.starter.generator.util.ProjectUtil.path;
import static java.util.Objects.requireNonNull;

public abstract class CopyProjectFiles implements ProjectGeneratorTask {

  private final Logger log = LoggerFactory.getLogger(CopyProjectFiles.class);

  protected final FileSystem fileSystem;
  protected final String modelDir;
  protected final SourceType sourceType;

    public CopyProjectFiles(FileSystem fileSystem, String modelDir, SourceType sourceType) {
    requireNonNull(fileSystem);
    requireNonNull(modelDir);
    requireNonNull(sourceType);
    this.fileSystem = fileSystem;
    this.modelDir = modelDir;
    this.sourceType = sourceType;
  }

  protected String sourcesDir(Project project) {
    return path("src", sourceType.getName(), project.getLanguage().getName());
  }

  protected String resourcesDir() {
    return path("src", sourceType.getName(), "resources");
  }

  protected String modelDir(Project project) {
    return path(modelDir, project.getModel());
  }
}
