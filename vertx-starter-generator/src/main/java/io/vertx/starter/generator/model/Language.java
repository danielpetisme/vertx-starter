package io.vertx.starter.generator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum  Language {
  @JsonProperty("java")
  JAVA("java"),

  @JsonProperty("kotlin")
  KOTLIN("kotlin");

  private final String name;

  Language(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
