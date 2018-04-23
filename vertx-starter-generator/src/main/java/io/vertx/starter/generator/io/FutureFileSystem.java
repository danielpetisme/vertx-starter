package io.vertx.starter.generator.io;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class FutureFileSystem {

  private final Logger log = LoggerFactory.getLogger(FutureFileSystem.class);

  private final Vertx vertx;

  public FutureFileSystem(Vertx vertx) {
    this.vertx = vertx;
  }

  public Future<String> writeFile(Path path, String content) {
    requireNonNull(path);
    requireNonNull(content);
    Future future = Future.future();
    String parent = path.getParent().toAbsolutePath().toString();
    boolean exists = vertx.fileSystem().existsBlocking(parent);
    if (!exists) {
      vertx.fileSystem().mkdirsBlocking(parent);
    }
    vertx.fileSystem().writeFile(path.toString(), Buffer.buffer(content), onFileWritten -> {
      if (onFileWritten.failed()) {
        log.error("Impossible to write file {} : {}", path, onFileWritten.cause().getMessage());
        future.fail(onFileWritten.cause());
      } else {
        log.info("File {} written", path);
        future.complete(path);
      }
    });
    return future;
  }

  public Future<String> copy(Path source, Path destination) {
    log.debug("Copying {} to {}", source, destination);
    Future future = Future.future();
    vertx.fileSystem().copy(source.toString(), destination.toString(), ar -> {
      if (ar.succeeded()) {
        future.complete();
      } else {
        log.error("Impossible to copy {} to {}: ", source, destination, ar.cause().getMessage());
        future.fail(ar.cause());
      }
    });
    return future;
  }

  public Future<String> mkdirs(String path) {
    Future future = Future.future();
    vertx.fileSystem().exists(path, onExistenceTested -> {
      if (onExistenceTested.succeeded()) {
        if (!onExistenceTested.result()) {
          vertx.fileSystem().mkdirs(path, onDirectoryCreated -> {
            if (onDirectoryCreated.succeeded()) {
              future.complete(path);
            } else {
              future.fail(onDirectoryCreated.cause());
            }
          });
        } else {
          log.debug("Directory {} already exists", path);
          future.complete(path);
        }
      } else {
        future.fail(onExistenceTested.cause());
      }
    });
    return future;
  }

  public Future<String> readFile(Path path) {
    Future future = Future.future();
    try {
      String content = Files.lines(path).collect(Collectors.joining(System.getProperty("line.separator")));
      future.complete(content);
    } catch (IOException e) {
      log.error("Impossible to read {}", path);
      future.fail(e);
    }
    return future;
  }

  public Future<Void> deleteRecursive(String path) {
    Future future = Future.future();
    vertx.fileSystem().deleteRecursive(path, true, ar -> {
      if (ar.failed()) {
        log.error("Impossible to delete directory {}: {}", path, ar.cause().getMessage());
        future.fail(ar.cause());
      } else {
        log.debug("Directory {} deleted", path);
        future.complete();
      }
    });
    return future;
  }

}
