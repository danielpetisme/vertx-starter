package io.vertx.starter.generator.io.impl;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.io.FileSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static io.vertx.core.Future.future;
import static java.util.Objects.requireNonNull;

public class FileSystemImpl implements FileSystem {

  private final Logger log = LoggerFactory.getLogger(FileSystemImpl.class);

  private final Vertx vertx;

  public FileSystemImpl(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  public Future<String> readFile(Path path) {
    log.debug("Reading file {}", path);
    requireNonNull(path);
    Future future = future();
    vertx.fileSystem().readFile(path.toString(), ar -> {
      if (ar.succeeded()) {
        future.complete(ar.result().toString());
        log.debug("File {} read", path);
      } else {
        future.fail(ar.cause());
        log.error("Impossible to read file {} : {}", path, ar.cause().getMessage());
      }
    });
    return future;
  }

  @Override
  public Future<Path> writeFile(Path destination, Buffer buffer) {
    log.debug("Writing file {}", destination);
    requireNonNull(destination);
    requireNonNull(buffer);
    return mkdirs(destination.getParent()).compose(path -> writeFileInternal(destination, buffer));
  }

  private Future<Path> writeFileInternal(Path destination, Buffer buffer) {
    Future future = future();
    vertx.fileSystem().writeFile(destination.toString(), buffer, ar -> {
      if (ar.succeeded()) {
        future.complete(destination);
        log.debug("File {} written", destination);
      } else {
        future.fail(ar.cause());
        log.error("Impossible to write file {} : {}", destination, ar.cause().getMessage());

      }
    });
    return future;
  }

  @Override
  public Future<Path> copy(Path source, Path destination) {
    log.debug("Copying {} to {} ", source, destination);
    requireNonNull(source);
    requireNonNull(destination);
    return readFile(source).compose(content -> this.writeFile(destination, Buffer.buffer(content)));
  }

  public Future<Boolean> exists(Path path) {
    log.debug("Testing existence of: {}", path);
    Future future = future();
    vertx.fileSystem().exists(path.toString(), ar -> {
      if (ar.succeeded()) {
        future.complete(ar.result());
        log.debug("Existence of {} tested", path);
      } else {
        future.fail(ar.cause());
        log.error("Impossible to test existence of: {}", path);
      }
    });
    return future;
  }

  public Future<Path> mkdirs(Path path) {
    log.debug("Creating path: {}", path);
    requireNonNull(path);
    return exists(path).compose(exists -> {
      Future future = future();
      if (!exists) {
        vertx.fileSystem().mkdirs(path.toString(), ar -> {
          if (ar.succeeded()) {
            future.complete(path);
            log.debug("Directory {} created", path);
          } else {
            future.fail(ar.cause());
            log.error("Impossible to create directory: {}", path);
          }
        });
      } else {
        future.complete(path);
      }
      return future;
    });
  }

  @Override
  public Future<Path> deleteRecursive(Path path) {
    log.debug("Deleting recursively path: {}", path);
    requireNonNull(path);
    Future future = Future.future();
    vertx.fileSystem().deleteRecursive(path.toString(), true, ar -> {
      if (ar.succeeded()) {
        future.complete();
        log.debug("Directory {} deleted", path);
      } else {
        future.fail(ar.cause());
        log.error("Impossible to delete directory {}: {}", path, ar.cause().getMessage());

      }
    });
    return future;
  }

  @Override
  public Future<Path> copyDir(Path source, Path destination) {
    log.debug("Copying directory: {} to {}", source, destination);
    requireNonNull(source);
    requireNonNull(destination);
    return mkdirs(destination).compose(path -> copyDirInternal(source, path));
  }

  private Future<Path> copyDirInternal(Path source, Path destination) {
    String parent = destination.getParent().toAbsolutePath().toString();
    Future future = future();
    vertx.fileSystem().copyRecursive(source.toString(), destination.toString(), true, ar -> {
      if (ar.succeeded()) {
        future.complete(destination);
        log.debug("Directory copied from {} to {}", source, destination);
      } else {
        future.fail(ar.cause());
        log.debug("Impossible to copy from {} to {} because {}", source, destination, ar.cause().getMessage());
      }
    });
    return future;
  }

  @Override
  public Future<List<Path>> readDir(Path path) {
    Future future = future();
    try {
      List<Path> sources = Files
        .walk(path)
        .filter(p -> Files.isRegularFile(p))
        .collect(Collectors.toList());
      future.complete(sources);
      log.debug("{} files founded", sources.size());
    } catch (IOException e) {
      future.fail(e);
      log.error("Impossible to read content of dir: {}", path);
    }
    return future;
  }

}
