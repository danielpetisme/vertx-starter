package io.vertx.starter.web.rest;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import io.vertx.starter.web.service.ProjectService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.vertx.starter.web.util.RestUtil.error;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.Arrays.asList;

public class ProjectResource {

  private static final Logger log = LoggerFactory.getLogger(ProjectResource.class);

  private static final List<String> DEFAULT_DEPENDENCIES = asList("vertx-core", "vertx-unit");

  private static final JsonObject DEFAULT_PROJECT_REQUEST = new JsonObject()
    .put("format", "zip")
    .put("language", "java")
    .put("build", "maven")
    .put("groupId", "io.vertx")
    .put("artifactId", "sample")
    .put("dependencies", new JsonArray(DEFAULT_DEPENDENCIES));

  private final ProjectService projectService;
  private final JsonObject defaults;

  public ProjectResource(JsonObject defaults, ProjectService projectService) {
    this.defaults = DEFAULT_PROJECT_REQUEST.mergeIn(defaults);
    this.projectService = projectService;
  }

  private String contentTypeHeader(String format) {
    if (format.equalsIgnoreCase("zip")) {
      return "application/zip";
    }
    return "";
  }

  public void create(RoutingContext rc) {
    JsonObject projectRequest = buildProjectRequest(rc.request());
    projectService.create(projectRequest, reply -> {
      if (reply.succeeded()) {
        JsonObject projectCreated = reply.result();
        File archive = new File(projectCreated.getString("archivePath"));
        String archiveName = projectCreated.getString("artifactId");
        rc.response()
          .setStatusCode(HTTP_OK)
          .putHeader("Content-Type", contentTypeHeader(projectCreated.getString("format")))
          .putHeader("Content-Disposition", "attachment; filename=" + archiveName + ".zip")
          .sendFile(archive.getAbsolutePath(), onFileSent -> {
            if (onFileSent.failed()) {
              log.error("Error: {}", onFileSent.cause().getMessage());
            }
          });
      } else {
        log.error("Failed to create project: {}" + reply.cause().getMessage());
        error(rc, reply.cause());
      }
    });
  }

  private JsonObject buildProjectRequest(HttpServerRequest request) {
    JsonObject userRequest = new JsonObject();
    String format = getArchiveFormat(request.path());
    if (format != null) {
      userRequest.put("format", format);
    }
    MultiMap params = request.params();
    if (isNotBlank(params.get("version"))) {
      userRequest.put("version", params.get("version"));
    }
    if (isNotBlank(params.get("language"))) {
      userRequest.put("language", params.get("language").toLowerCase());
    }
    if (isNotBlank(params.get("build"))) {
      userRequest.put("build", params.get("build").toLowerCase());
    }
    if (isNotBlank(params.get("groupId"))) {
      userRequest.put("groupId", params.get("groupId"));
    }
    if (isNotBlank(params.get("artifactId"))) {
      userRequest.put("artifactId", params.get("artifactId"));
    }
    if (isNotBlank(params.get("dependencies"))) {
      Set<String> dependencies = new HashSet<>(DEFAULT_DEPENDENCIES);
      for (String dependency : params.get("dependencies").split(",")) {
        dependencies.add(dependency.toLowerCase());
      }
      userRequest.put("dependencies", new JsonArray(new ArrayList(dependencies)));
    }
    return this.defaults.copy().mergeIn(userRequest);
  }

  private String getArchiveFormat(String path) {
    if (path.matches(".*\\.zip$")) {
      return "zip";
    }
    if (path.matches(".*(\\.tar\\.gz|\\.tgz)$")) {
      return "tgz";
    }
    return null;
  }

  private boolean isNotBlank(String value) {
    return value != null && value.length() > 0;
  }
}
