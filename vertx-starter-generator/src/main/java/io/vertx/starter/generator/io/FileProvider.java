package io.vertx.starter.generator.io;

import io.vertx.core.Future;

import java.nio.file.Path;
import java.util.List;

public interface FileProvider {

  Future<Path> resolve(String location);

  Future<List<SourceFile>> getSourceFiles(Path location);

}
