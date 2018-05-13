package io.vertx.starter.generator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum  Language {
  @JsonProperty("java")
  JAVA("java", ".java"),

  @JsonProperty("kotlin")
  KOTLIN("kotlin", ".kt");

  private final String name;
    private final String extension;

    Language(String name, String extension) {
        this.name = name;
        this.extension = extension;
    }

    public String getName() {
    return name;
  }

    public String getExtension() {
        return extension;
    }
}
