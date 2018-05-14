package io.vertx.starter.generator.engine;

import com.github.jknack.handlebars.io.TemplateLoader;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.tasks.CopyFile;
import io.vertx.starter.generator.engine.tasks.ProcessSources;
import io.vertx.starter.generator.engine.tasks.Render;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class ProjectGeneratorImpl implements ProjectGenerator {

    private final Logger log = LoggerFactory.getLogger(ProjectGeneratorImpl.class);

    private final TemplateLoader templateLoader = null;
    private final FileSystem fileSystem;
    private final List<Function<Project, Task>> tasks = new ArrayList<>();
    private ProjectGeneratorConfiguration configuration;

    public ProjectGeneratorImpl(FileSystem fileSystem, ProjectGeneratorConfiguration configuration) {
        this.fileSystem = fileSystem;
        this.configuration = configuration;
    }

    @Override
    public Future<Project> generate(Project project) {
        log.debug("Request to run {} tasks to generate project: {}", tasks.size(), project);
        if (project.getModel() == null || project.getModel().isEmpty()) {
            project.setModel(configuration.getDefaultModel());
        }
        project.setProjectDir(Paths.get(configuration.getModelDir()).resolve(project.getModel()));
        project.setOutputDir(Paths.get(configuration.getTmpDir()).resolve(UUID.randomUUID().toString()).resolve(project.getArtifactId()));

        List<Future> futures = tasks
            .stream()
            .map(function -> function.apply(project))
            .map(task -> task.execute())
            .collect(Collectors.toList());
        return CompositeFuture.all(futures)
            .compose(it -> Future.succeededFuture(project));
    }

    @Override
    public ProjectGenerator processMainSources() {
        tasks.add(project -> new ProcessSources(project, fileSystem).sourceSet(project.getMainSourceSet()));
        return this;
    }

    @Override
    public ProjectGenerator processTestSources() {
        tasks.add(project -> new ProcessSources(project, fileSystem).sourceSet(project.getTestSourceSet()));
        return this;
    }

    @Override
    public ProjectGenerator copyFile(String source, String destination) {
        requireNonNull(source);
        requireNonNull(destination);
        tasks.add(project -> new CopyFile(project, fileSystem).source(source).destination(destination));
        return this;
    }

    @Override
    public ProjectGenerator render(String template, String destination) {
        requireNonNull(template);
        requireNonNull(destination);
        tasks.add(project -> new Render(project, templateLoader, fileSystem).template(template).destination(destination));
        return this;
    }

}
