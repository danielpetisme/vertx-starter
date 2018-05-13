package io.vertx.starter.generator.io;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.starter.generator.io.impl.FileSystemImpl;

import java.nio.file.Path;
import java.util.List;

public interface FileSystem {

  static FileSystem fileSystem(Vertx vertx) {
    return new FileSystemImpl(vertx);
  }

  Future<String> readFile(Path path);

    Future<List<String>> readDir(Path path);

  Future<Path> writeFile(Path path, Buffer buffer);

  Future<Path> copy(Path source, Path destination);

  Future<Boolean> exists(Path path);

  Future<Path> mkdirs(Path path);

  Future<Path> deleteRecursive(Path path);

  Future<Path> copyDir(Path source, Path destination);
}
