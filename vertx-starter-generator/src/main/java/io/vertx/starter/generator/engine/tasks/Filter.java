package io.vertx.starter.generator.engine.tasks;

import io.vertx.core.Future;
import io.vertx.starter.generator.engine.SourceType;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

import java.util.function.Function;

public class Filter extends Copy {

    private Function<String, String> filter;

    public Filter(FileSystem fileSystem, String modelDir, SourceType sourceType, String defaultPackage) {
        super(fileSystem, modelDir, sourceType, defaultPackage);
    }

    @Override
    public Future execute(Project project) {
        return null;
    }
}
