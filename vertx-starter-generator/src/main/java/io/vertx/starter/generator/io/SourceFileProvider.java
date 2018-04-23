package io.vertx.starter.generator.io;

import io.vertx.core.Future;
import io.vertx.starter.generator.domain.ProjectSourceFiles;

import java.nio.file.Path;

public interface SourceFileProvider {

  Future<Path> resolve(String location);

  Future<ProjectSourceFiles> getProjectSourceFiles(String location);

}
