package io.vertx.starter.generator.util;

import io.vertx.starter.generator.domain.Project;

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

  public static String sourceDir(Project project) {
    return format("src/main/%s/%s", project.getLanguage().getName(), packageDir(project));
  }

  public static String projectSourceDir(String baseDir, Project project) {
    return path(baseDir, sourceDir(project));
  }

}
