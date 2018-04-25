package io.vertx.starter.generator.engine;

public enum  SourceType {
  MAIN("main"), TEST("test");

  private final String name;

  SourceType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
