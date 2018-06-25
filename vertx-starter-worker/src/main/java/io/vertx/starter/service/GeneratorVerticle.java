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
package io.vertx.starter.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.service.handler.ProjectHandler;
import io.vertx.starter.service.service.ProjectGeneratorService;

public class GeneratorVerticle extends AbstractVerticle {

  private final Logger log = LoggerFactory.getLogger(GeneratorVerticle.class);

  @Override
  public void start(Future<Void> startFuture) {
    ProjectGeneratorService projectGeneratorService = new ProjectGeneratorService(vertx);
    ProjectHandler projectHandler = new ProjectHandler(projectGeneratorService);
    vertx.eventBus().<JsonObject>consumer("project.requested").handler(projectHandler::onProjectRequested);
    vertx.eventBus().<JsonObject>consumer("project.created").handler(projectHandler::onProjectCreated);

    log.info(
      "\n----------------------------------------------------------\n\t" +
        "{} is running!\n" +
        "----------------------------------------------------------",
      GeneratorVerticle.class.getSimpleName()
    );

    startFuture.complete();
  }
}
