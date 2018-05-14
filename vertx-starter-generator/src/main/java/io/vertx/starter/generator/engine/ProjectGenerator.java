package io.vertx.starter.generator.engine;

import io.vertx.core.Future;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

public interface ProjectGenerator {

    static ProjectGenerator createProjectGenerator(FileSystem fileSystem, ProjectGeneratorConfiguration configuration) {
        return new ProjectGeneratorImpl(fileSystem, configuration);
    }

    static ProjectGenerator createProjectGenerator(FileSystem fileSystem) {
        return createProjectGenerator(fileSystem, new ProjectGeneratorConfiguration());
    }

    ProjectGenerator copyFile(String source, String destination);

    ProjectGenerator render(String template, String destination);

    ProjectGenerator processMainSources();

    ProjectGenerator processTestSources();

    Future<Project> generate(Project project);
}
