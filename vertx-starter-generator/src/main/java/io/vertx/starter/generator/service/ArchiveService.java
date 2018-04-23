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
import io.vertx.starter.generator.domain.Format;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;

public class ArchiveService {

  private final Logger log = LoggerFactory.getLogger(ArchiveService.class);

  public Future<String> archive(String rootDir, Format format) {
    return zip (rootDir);
  }

  private Future<String> zip(String rootDir) {
    Future future = Future.future();
    String archive = rootDir + "/archive.zip";
    ZipUtil.pack(new File(rootDir), new File(archive), false);
    future.complete(archive);
    return future;
  }
}
