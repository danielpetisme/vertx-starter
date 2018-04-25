package io.vertx.starter.generator.engine.tasks;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.SourceType;
import io.vertx.starter.generator.io.FileProvider;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.io.SourceFile;
import io.vertx.starter.generator.model.Project;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static io.vertx.starter.generator.util.ProjectUtil.*;
import static java.util.Objects.requireNonNull;

public class CopySources extends CopyProjectFiles {

  private final Logger log = LoggerFactory.getLogger(CopyTextFile.class);

  private final String defaultPackage;

  public CopySources(FileProvider fileProvider, FileSystem fileSystem, String modelDir, SourceType sourceType, String defaultPackage) {
    super(fileProvider, fileSystem, modelDir, sourceType);
    requireNonNull(defaultPackage);
    this.defaultPackage = defaultPackage;
  }

  @Override
  public Future execute(Project project) {
    log.debug("Request to copy sources for project: {}", project);
    requireNonNull(project);
    String sourcesDir = sourcesDir(project);
    Path destination = project.getTargetDir().resolve(sourcesDir);
    return fileProvider.resolve(path(modelDir(project), sourcesDir(project)))
      .compose(fileProvider::getSourceFiles)
      .compose(sources -> CompositeFuture.all(copySources(sources, destination, project)));
  }

  private List<Future> copySources(List<SourceFile> sources, Path destination, Project project) {
    log.debug("Copying {} copy sources to: {} for project: {}", sources.size(), destination, project);
    String sourcePackageDir = defaultPackage.replaceAll("\\.", "/");
    String destinationPackageDir = packageDir(project);
    return sources
      .stream()
      .map(sourceFile -> fileSystem
        .readFile(sourceFile.getPath())
        .compose(content -> Future.succeededFuture(updatePackage(content, project)))
        .compose(content ->
          fileSystem.writeFile(
            destination.resolve(sourceFile.getFilename().replaceAll(sourcePackageDir, destinationPackageDir)),
            Buffer.buffer(content)
          )
        )).collect(Collectors.toList());
  }

  private String updatePackage(String sourceContent, Project project) {
    return sourceContent.replaceAll("^package " + defaultPackage + "(.*)", "package " + projectPackage(project) +"$1");
  }

}
