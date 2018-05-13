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
import io.vertx.starter.generator.model.Format;
import io.vertx.starter.generator.model.Project;
import org.zeroturnaround.zip.ZipUtil;

import java.nio.file.Path;

import static java.lang.String.format;

public class ArchiveService {

  private final Logger log = LoggerFactory.getLogger(ArchiveService.class);

  public static final String DEFAULT_ARCHIVE_NAME = "archive";

  public Future<Path> archive(Project project) {
    Path archivePath = archivePath(project.getBaseDir(), project.getFormat());
    switch (project.getFormat()) {
      default:
          return zip(project.getOutputDir(), archivePath);
    }
  }

  private Path archivePath(Path targetDir, Format format) {
    return targetDir.resolve(format("%s.%s", DEFAULT_ARCHIVE_NAME, format.getFileExtension()));
  }

  private Future<Path> zip(Path sourceDir, Path archivePath) {
    log.debug("Generating archive from {} to {}", sourceDir, archivePath);
    Future future = Future.future();
    ZipUtil.pack(sourceDir.toFile(),archivePath.toFile(), true);
    future.complete(archivePath);
    return future;
  }
}
