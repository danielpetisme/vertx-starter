package io.vertx.starter.generator.domain;

import java.nio.file.Path;
import java.util.List;

public class ProjectSourceFiles {

  public final Path baseDir;
  public final List<String> files;

  public ProjectSourceFiles(Path baseDir, List<String> files) {
    this.baseDir = baseDir;
    this.files = files;
  }
}
