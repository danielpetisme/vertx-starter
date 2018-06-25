package io.vertx.starter.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Build {
  @JsonProperty("maven")
  MAVEN,
  @JsonProperty("gradle")
  GRADLE
}
