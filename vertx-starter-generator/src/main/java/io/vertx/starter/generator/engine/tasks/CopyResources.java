package io.vertx.starter.generator.engine.tasks;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.SourceType;
import io.vertx.starter.generator.io.FileProvider;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

import static io.vertx.starter.generator.util.ProjectUtil.path;
import static java.util.Objects.requireNonNull;

public class CopyResources extends CopyProjectFiles {

  private final Logger log = LoggerFactory.getLogger(CopyTextFile.class);

  public CopyResources(FileProvider fileProvider, FileSystem fileSystem, String modelDir, SourceType sourceType) {
    super(fileProvider, fileSystem, modelDir, sourceType);
  }

  @Override
  public Future execute(Project project) {
    log.debug("Request to copy {} resources directory", sourceType);
    requireNonNull(project);
    String resourcesDir = resourcesDir();
    return fileProvider
      .resolve(path(modelDir(project), resourcesDir))
      .otherwiseEmpty()
      .compose(path -> {
        if (path != null) {
          return fileSystem.copyDir(path, project.getTargetDir().resolve(resourcesDir));
        }
        return Future.succeededFuture();
      });
  }
}
