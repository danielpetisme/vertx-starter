package io.vertx.starter.generator.engine;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.io.impl.ClassPathSourceProvider;
import io.vertx.starter.generator.model.Project;

public interface ProjectGenerator {

  static ProjectGenerator defaultProjectGenerator(Vertx vertx) {
    return new ProjectGeneratorBuilder()
      .setDefaultPackage("io.vertx.example")
      .setModelDir("/projects")
      .setDefaultModel("basic")
      .setFileSystem(FileSystem.fileSystem(vertx))
      .setFileProvider(new ClassPathSourceProvider())
      .setTemplateLoader(new ClassPathTemplateLoader("/templates"))
      .build();
  }

  Future<Project> generate(Project project);

  ProjectGenerator copySources();

  ProjectGenerator copyFile(String source, String destination);

  ProjectGenerator render(String template, String destination);

  ProjectGenerator copyMainSources();

  ProjectGenerator copyMainResources();

  ProjectGenerator copyTestSources();

  ProjectGenerator copyTestResources();
}
