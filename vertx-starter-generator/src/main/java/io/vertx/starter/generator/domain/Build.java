package io.vertx.starter.generator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum  Build {
  @JsonProperty("maven")
  MAVEN,
  @JsonProperty("gradle")
  GRADLE
}
