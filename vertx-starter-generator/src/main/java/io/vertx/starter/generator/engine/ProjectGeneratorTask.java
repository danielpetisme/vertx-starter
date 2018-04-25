package io.vertx.starter.generator.engine;

import io.vertx.core.Future;
import io.vertx.starter.generator.model.Project;

public interface ProjectGeneratorTask {

  Future execute(Project project);
}
