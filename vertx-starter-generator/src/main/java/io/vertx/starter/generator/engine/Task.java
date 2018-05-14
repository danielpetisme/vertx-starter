package io.vertx.starter.generator.engine;

import io.vertx.core.Future;
import io.vertx.starter.generator.model.Project;

public abstract class Task {

    private final Project project;

    public Task(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public abstract Future<Project> execute();

}
