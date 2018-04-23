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

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.domain.Build;
import io.vertx.starter.generator.domain.Project;
import io.vertx.starter.generator.domain.ProjectSourceFiles;
import io.vertx.starter.generator.io.FutureFileSystem;
import io.vertx.starter.generator.io.SourceFileProvider;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static io.vertx.starter.generator.util.ProjectUtil.*;
import static java.util.Arrays.asList;

public class ProjectGeneratorService {

  private final Logger log = LoggerFactory.getLogger(ProjectGeneratorService.class);
  public static final String DEFAULT_PACKAGE_ROOT = "io.vertx.example";
  public static final String DEFAULT_PACKAGE_DIR = "io/vertx/example";

  private final SourceFileProvider sourceFileLoader;
  private final FutureFileSystem fileSystem;
  private final TemplateService templateService;

  public ProjectGeneratorService(SourceFileProvider sourceFileLoader, FutureFileSystem fileSystem, TemplateService templateService) {
    this.sourceFileLoader = sourceFileLoader;
    this.fileSystem = fileSystem;
    this.templateService = templateService;
  }

  public CompositeFuture generate(Project project, Path basedir) {
    return CompositeFuture.all(projectFiles(project));
  }

  public List<Future> projectFiles(Path baseDir, Project project) {
    log.info("Generating project files for project: {} to base dir: {}", project, baseDir);
    List<Future> futures = new ArrayList<>();
    futures.addAll(basicProjectFiles(baseDir, project));
    futures.addAll(buildFiles(baseDir, project));
    futures.add(sourceFiles(baseDir, project));
    return futures;
  }

  public List<Future<String>> basicProjectFiles(Path baseDir, Project project) {
    log.info("Generating basic project files for project: {} to base dir: {}", project, baseDir);
    return asList(
      render("_gitignore", path(baseDir, ".gitignore"), project),
      copy("_editorconfig", path(baseDir, ".editorconfig"))
    );
  }

  public List<Future<String>> buildFiles(String baseDir, Project project) {
    log.info("Generating build files for project: {} to base dir: {}", project, baseDir);
    if (project.getBuild() == Build.MAVEN) {
      return asList(
        copy("_mvn/wrapper/maven-wrapper.jar", path(baseDir, ".mvn/wrapper/maven-wrapper.jar")),
        copy("_mvn/wrapper/maven-wrapper.properties", path(baseDir, ".mvn/wrapper/maven-wrapper.properties")),
        copy("mvnw", path(baseDir, "mvnw")),
        copy("mvnw.bat", path(baseDir, "mvnw.bat")),
        render("pom.xml", path(baseDir, "pom.xml"), project)
      );
    }
    if (project.getBuild() == Build.GRADLE) {
      return asList(
        copy("gradle/wrapper/gradle-wrapper.jar", path(baseDir, "gradle/wrapper/gradle-wrapper.jar")),
        copy("gradle/wrapper/gradle-wrapper.properties", path(baseDir, "gradle/wrapper/gradle-wrapper.properties")),
        copy("gradlew", path(baseDir, "gradlew")),
        copy("gradlew.bat", path(baseDir, "gradlew.bat")),
        render("build.gradle", path(baseDir, "build.gradle"), project),
        render("settings.gradle", path(baseDir, "settings.gradle"), project)
      );
    }
    return asList();

  }

  public Future sourceFiles(String baseDir, Project project) {
    log.info("Generating source files for project: {} to base dir: {}", project, baseDir);
    return sourceFileLoader
      .getProjectSourceFiles("projects/core").compose(projectSourceFiles -> copyProjectSources(projectSourceFiles, baseDir, project));
  }

  private Future copyProjectSources(ProjectSourceFiles projectSourceFiles, String baseDir, Project project) {
    List<Future> projectSources = new ArrayList<>();
    for (String filename: projectSourceFiles.files) {
      Path filePath = projectSourceFiles.baseDir.resolve(filename);
      String destination = filename.replaceAll(DEFAULT_PACKAGE_DIR, packageDir(project));
      log.info("Copying {} to {}", filePath, destination);
      projectSources.add(fileSystem
        .readFile(filePath)
        .compose(content -> fileSystem.writeFile(path(baseDir, destination), content)));

    }
    return CompositeFuture.all(projectSources);
  }

  private Future<String> render(String template, String destination, Project project) {
    log.info("Rendering  template: {} to destination:{} with project: {}", template, destination, project);
    return templateService.render(template, project).compose(content -> fileSystem.writeFile(destination, content));
  }

  private Future<String> copy(String source, Path projectBaseDir) {
    log.info("Copying source: {} to project base dir:{}", source, projectBaseDir);
    return sourceFileLoader
      .resolve(source)
      .compose(fileSystem::readFile)
      .compose(content -> fileSystem.writeFile(projectBaseDir.resolve(source), content));
  }

}
