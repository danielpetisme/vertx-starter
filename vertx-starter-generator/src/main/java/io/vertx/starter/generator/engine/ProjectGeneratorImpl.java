package io.vertx.starter.generator.engine;

import com.github.jknack.handlebars.io.TemplateLoader;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.tasks.Copy;
import io.vertx.starter.generator.engine.tasks.CopyDirectory;
import io.vertx.starter.generator.engine.tasks.CopyFile;
import io.vertx.starter.generator.engine.tasks.RenderTemplate;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class ProjectGeneratorImpl implements ProjectGenerator {

    private final Logger log = LoggerFactory.getLogger(ProjectGeneratorImpl.class);

    private final TemplateLoader templateLoader;
    private final FileSystem fileSystem;


    private final String defaultModel;
    private final String defaultPackage;
    private final String modelDir;

    private final String tmpDir;

    private final List<ProjectGeneratorTask> tasks = new ArrayList<>();

    public ProjectGeneratorImpl(TemplateLoader templateLoader, FileSystem fileSystem, String defaultModel, String defaultPackage, String modelDir, String tmpDir) {
        this.templateLoader = templateLoader;
        this.fileSystem = fileSystem;
        this.defaultModel = defaultModel;
        this.defaultPackage = defaultPackage;
        this.modelDir = modelDir;
        this.tmpDir = tmpDir;
    }

    @Override
    public Future<Project> generate(Project project) {
        log.debug("Request to run {} tasks to generate project: {}", tasks.size(), project);
        if (project.getModel() == null || project.getModel().isEmpty()) {
            project.setModel(defaultModel);
        }
        project.setProjectDir(Paths.get(modelDir).resolve(project.getModel()));
        project.setOutputDir(Paths.get(tmpDir).resolve(UUID.randomUUID().toString()).resolve(project.getArtifactId()));

        List<Future> futures = tasks.stream().map(task -> task.execute(project)).collect(Collectors.toList());
        return CompositeFuture.all(futures)
            .compose(it -> Future.succeededFuture(project));
    }

    @Override
    public ProjectGenerator copySources() {
        return copyMainSources();
//      .copyMainResources()
//      .copyTestSources();
//      .copyTestResources();
    }

    @Override
    public ProjectGenerator copyFile(String source, String destination) {
        requireNonNull(source);
        requireNonNull(destination);
        tasks.add(new CopyFile(fileSystem, source, destination));
        return this;
    }

    @Override
    public ProjectGenerator render(String template, String destination) {
        requireNonNull(template);
        requireNonNull(destination);
        tasks.add(new RenderTemplate(templateLoader, fileSystem, template, destination));
        return this;
    }

    @Override
    public ProjectGenerator copyMainSources() {
        tasks.add(new Copy(fileSystem, SourceType.MAIN, defaultPackage));
        return this;
    }

    @Override
    public ProjectGenerator copyMainResources() {
        new Copy(
            fileSystem,
            (String path) -> "",
            (String content) -> ""
        );
        tasks.add(new CopyDirectory(fileSystem, modelDir, SourceType.MAIN));
        return this;
    }

    @Override
    public ProjectGenerator copyTestSources() {
        tasks.add(new Copy(fileSystem, modelDir, SourceType.TEST, defaultPackage));
        return this;
    }

    @Override
    public ProjectGenerator copyTestResources() {
        tasks.add(new CopyDirectory(fileSystem, modelDir, SourceType.TEST));
        return this;
    }

    @Override
    public ProjectGenerator processMainSources(Project project) {
        tasks.add(new Copy(
            fileSystem,
            project.getProjectDir(),
            project.getOutputDir(),
            (String filename) -> "",
            (String content) -> content
        ));
        return this;
    }
}
