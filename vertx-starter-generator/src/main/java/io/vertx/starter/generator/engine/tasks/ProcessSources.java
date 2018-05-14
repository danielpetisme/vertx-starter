package io.vertx.starter.generator.engine.tasks;

import io.vertx.core.Future;
import io.vertx.starter.generator.engine.Task;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

public class ProcessSources extends Task {

    private CopyDirectory copyDirectory;
    private String sourceSet;

    public ProcessSources(Project project, FileSystem fileSystem) {
        super(project);
        copyDirectory = new CopyDirectory(project, fileSystem)
            .rename(this::updateFileName)
            .filter(this::updatePackage);
    }

    public String sourceSet() {
        return sourceSet;
    }

    public ProcessSources sourceSet(String sourceSet) {
        this.sourceSet = sourceSet;
        return this;
    }

    @Override
    public Future execute() {
        return copyDirectory.from(sourceSet).into(sourceSet).execute();
    }

    private String updateFileName(String filename) {
        return null;
    }

    private String updatePackage(String content) {
        return null;
    }

}
