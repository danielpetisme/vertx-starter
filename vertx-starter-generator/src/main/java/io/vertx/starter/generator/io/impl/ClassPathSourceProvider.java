package io.vertx.starter.generator.io.impl;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.domain.ProjectSourceFiles;
import io.vertx.starter.generator.io.SourceFileProvider;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ClassPathSourceProvider implements SourceFileProvider {

  private final Logger log = LoggerFactory.getLogger(ClassPathSourceProvider.class);

  public static final Path DEFAULT_SOURCE_DIR = Paths.get(ClassPathSourceProvider.class.getResource("/projects/core").getPath());

  private final String prefix;

  public ClassPathSourceProvider(String prefix) {
    this.prefix = prefix;
  }

  public ClassPathSourceProvider() {
    this("/");
  }

  private String prefix(String location) {
    if (location.startsWith("/")) {
      return location;
    }
    return "/" + location;
  }

  @Override
  public Future<Path> resolve(String location) {
    Future future = Future.future();
    try {
      Path path = Paths.get(getClass().getResource(prefix(location)).toURI());
      future.complete(path);
    } catch (URISyntaxException e) {
      log.error("Impossible to get path: {}", location);
      future.fail(e);
    }
    return future;
  }

  public Future<ProjectSourceFiles> getProjectSourceFiles(String location) {
    return resolve(location).compose(this::getSourcesInternal);
  }

  private Future<ProjectSourceFiles> getSourcesInternal(Path path) {
    Future future = Future.future();
    try {
      List<String> sources = Files
        .walk(path)
        .filter(p -> Files.isRegularFile(p))
        .map(p -> p.toString().replaceAll(path.toString() + "/", ""))
        .collect(Collectors.toList());
      ProjectSourceFiles projectSourceFiles = new ProjectSourceFiles(path, sources);
      future.complete(projectSourceFiles);
    } catch (IOException e) {
      log.error("Impossible to read content of dir: {}", DEFAULT_SOURCE_DIR);
      future.fail(e);
    }
    return future;
  }

}
