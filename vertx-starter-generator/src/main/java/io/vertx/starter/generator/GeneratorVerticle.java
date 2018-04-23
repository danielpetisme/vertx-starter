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
package io.vertx.starter.generator;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.handler.ProjectCreatedHandler;
import io.vertx.starter.generator.handler.ProjectRequestedHandler;
import io.vertx.starter.generator.io.FutureFileSystem;
import io.vertx.starter.generator.io.impl.ClassPathSourceProvider;
import io.vertx.starter.generator.service.ArchiveService;
import io.vertx.starter.generator.service.ProjectGeneratorService;
import io.vertx.starter.generator.service.TemplateService;

public class GeneratorVerticle extends AbstractVerticle {

  public static final String TEMPLATE_DIR = "/templates";
  public static final String PROJECTS_ROOT_DIR = "/projects/core";

  private final Logger log = LoggerFactory.getLogger(GeneratorVerticle.class);

  private String tempDir() {
    return config().getString("temp.dir", System.getProperty("java.io.tmpdir"));
  }

  private String projectsDir() {
    return config().getString("projects.dir", PROJECTS_ROOT_DIR);
  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    String rootDir = tempDir();
    FutureFileSystem fileSystem = new FutureFileSystem(vertx);
    fileSystem
      .mkdirs(rootDir)
      .setHandler(ar -> {
        if (ar.succeeded()) {

          TemplateLoader loader = new ClassPathTemplateLoader(TEMPLATE_DIR);
          ProjectGeneratorService generatorService = new ProjectGeneratorService(new ClassPathSourceProvider(), fileSystem, new TemplateService(loader));
          ArchiveService archiveService = new ArchiveService();
          ProjectRequestedHandler projectRequestedHandler = new ProjectRequestedHandler(rootDir, generatorService, archiveService);
          ProjectCreatedHandler projectCreatedHandler = new ProjectCreatedHandler(fileSystem);

          vertx.eventBus().<JsonObject>consumer("project.requested").handler(projectRequestedHandler::handle);
          vertx.eventBus().<String>consumer("project.created").handler(projectCreatedHandler::handle);

          log.info("\n----------------------------------------------------------\n\t" +
              "{} is running!\n" +
              "----------------------------------------------------------",
            GeneratorVerticle.class.getSimpleName());

          startFuture.complete();
        } else {
          log.error("Generator will shutdown...");
          startFuture.fail(ar.cause());
        }
      });
  }
}
