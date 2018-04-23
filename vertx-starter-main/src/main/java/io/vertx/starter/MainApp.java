package io.vertx.starter;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class MainApp {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    JsonObject config = vertx.fileSystem().readFileBlocking("/Users/daniel/workspace/vertx-starter/vertx-starter-main/conf/default-conf.json").toJsonObject();
    vertx.deployVerticle(MainVerticle.class.getName(), new DeploymentOptions().setConfig(config));
  }
}
