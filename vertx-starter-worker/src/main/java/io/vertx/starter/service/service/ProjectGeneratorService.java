/*
 * Copyright (c) 2017 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.starter.service.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.service.model.Project;
import org.gradle.tooling.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ProjectGeneratorService {

  private final Logger log = LoggerFactory.getLogger(ProjectGeneratorService.class);

  private final Vertx vertx;
  private final ProjectConnection connection;

  public ProjectGeneratorService(Vertx vertx) {
    this.vertx = vertx;
    GradleConnector connector = GradleConnector.newConnector();
    File file = new File("/Users/daniel/workspace/vertx-starter/vertx-starter-projects");
    connector.forProjectDirectory(file);
    this.connection = connector.connect();
  }

  public Future<String> buildProject(Project project) {
    Future future = Future.future();
    vertx.executeBlocking(blockingBuildFuture -> {
      // Configure the build
      BuildLauncher launcher = connection.newBuild();
      List<String> args = Arrays.asList(
//                "--stacktrace",
//                "--debug",
        "--rerun-tasks",
        "-Dorg.gradle.project.buildDir=/Users/daniel/workspace/vertx-starter/vertx-starter-projects/build/",
        "-PgroupId=" + project.getGroupId()
      );
      launcher.withArguments(args);
      launcher.setStandardOutput(System.out);
      launcher.setStandardError(System.err);
      // Run the build
      launcher.run(new ResultHandler<Void>() {
        @Override
        public void onComplete(Void aVoid) {
          String archivePath = "/Users/daniel/workspace/vertx-starter/vertx-starter-projects/build/" + project.getArtifactId() + ".zip";
          blockingBuildFuture.complete(archivePath);
        }

        @Override
        public void onFailure(GradleConnectionException e) {
          blockingBuildFuture.fail(e);
        }
      });
    }, res -> {
      if (res.succeeded()) {
        future.complete(res.result());
      } else {
        log.error("Failed to generate project {}", res.cause().getMessage());
        future.fail(res.cause());
      }
    });
    return future;
  }

  public Future cleanProject(Project project) {
    return null;
  }
}

