package io.vertx.starter.generator.io.impl;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.io.FileProvider;
import io.vertx.starter.generator.io.SourceFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ClassPathSourceProvider implements FileProvider {

  private final Logger log = LoggerFactory.getLogger(ClassPathSourceProvider.class);

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
      URL resource = getClass().getResource(prefix(location));
      if(resource != null) {
        Path path = Paths.get(resource.toURI());
        future.complete(path);
      } else {
        String message = location + " does not exists";
        future.fail(message);
        log.warn(message);
      }
    } catch (URISyntaxException e) {
      future.fail(e);
      log.error("Impossible to get path: {}", location);
    }
    return future;
  }

  @Override
  public Future<List<SourceFile>> getSourceFiles(Path path) {
    Future future = Future.future();
    try {
      List<SourceFile> sources = Files
        .walk(path)
        .filter(p -> Files.isRegularFile(p))
        .map(p -> new SourceFile(p, p.toString().replaceAll(path.toString() + "/", "")))
        .collect(Collectors.toList());
      future.complete(sources);
    } catch (IOException e) {
      future.fail(e);
      log.error("Impossible to read content of dir: {}", path);
    }
    return future;
  }

}
