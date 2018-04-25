package io.vertx.starter.generator.util;

import io.vertx.starter.generator.model.Project;

import static java.lang.String.format;

public final class ProjectUtil {

  public static String path(String baseDir, String... children) {
    return baseDir + "/" + String.join("/", children);
  }

  public static String packageDir(Project project) {
    return format("%s/%s", project.getGroupId().replaceAll("\\.", "/"), project.getArtifactId());
  }

  public static String projectPackage(Project project) {
    return format("%s.%s", project.getGroupId(), project.getArtifactId());
  }

}
