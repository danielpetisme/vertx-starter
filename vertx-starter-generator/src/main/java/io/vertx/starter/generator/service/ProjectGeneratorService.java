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
package io.vertx.starter.generator.service;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.ProjectGenerator;
import io.vertx.starter.generator.model.Project;

public class ProjectGeneratorService {

  private final Logger log = LoggerFactory.getLogger(ProjectGeneratorService.class);

  private final ProjectGenerator generator;

  public ProjectGeneratorService(ProjectGenerator generator) {
    this.generator = generator;
  }

  public Future<Project> generate(Project project) {
      return Future.succeededFuture();
//    log.info("Generating project: {}", project);
//    generator
//      .render("_gitignore", ".gitignore")
//      .copyFile("_editorconfig", ".editorconfig");
//    if (project.getBuild() == Build.MAVEN) {
//      generator
//        .copyFile("_mvn/wrapper/maven-wrapper.jar", ".mvn/wrapper/maven-wrapper.jar")
//        .copyFile("_mvn/wrapper/maven-wrapper.properties", ".mvn/wrapper/maven-wrapper.properties")
//        .copyFile("mvnw", "mvnw")
//        .copyFile("mvnw.bat", "mvnw.bat")
//        .render("pom.xml", "pom.xml");
//    }
//    if (project.getBuild() == Build.GRADLE) {
//      generator
//        .copyFile("gradle/wrapper/gradle-wrapper.jar", "gradle/wrapper/gradle-wrapper.jar")
//        .copyFile("gradle/wrapper/gradle-wrapper.properties", "gradle/wrapper/gradle-wrapper.properties")
//        .copyFile("gradlew", "gradlew")
//        .copyFile("gradlew.bat", "gradlew.bat")
//        .render("build.gradle", "build.gradle")
//        .render("settings.gradle", "settings.gradle");
//    }
//      generator
//          .processMainSources();
//    generator.copySources();
//
//      return generator.generate();
  }
}

