package io.vertx.starter.generator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Format {
  @JsonProperty("zip")
  ZIP("zip");

  private final String fileExtension;

  Format(String fileExtension) {
    this.fileExtension = fileExtension;
  }

  public String getFileExtension() {
    return fileExtension;
  }
}
