package io.vertx.starter.generator.util;

public final class ProjectUtil {

  public static String path(String baseDir, String... children) {
    return baseDir + "/" + String.join("/", children);
  }

}
