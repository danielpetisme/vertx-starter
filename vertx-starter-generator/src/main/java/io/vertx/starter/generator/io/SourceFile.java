package io.vertx.starter.generator.io;

import java.nio.file.Path;
import java.util.List;

public class SourceFile {

  private final Path path;
  private final String filename;

  public SourceFile(Path path, String filename) {
    this.path = path;
    this.filename = filename;
  }

  public Path getPath() {
    return path;
  }

  public String getFilename() {
    return filename;
  }
}
