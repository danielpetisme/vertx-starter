package io.vertx.starter.generator.engine;

import com.github.jknack.handlebars.io.TemplateLoader;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.tasks.CopyResources;
import io.vertx.starter.generator.engine.tasks.CopySources;
import io.vertx.starter.generator.engine.tasks.CopyTextFile;
import io.vertx.starter.generator.engine.tasks.RenderTemplate;
import io.vertx.starter.generator.io.FileProvider;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class ProjectGeneratorImpl implements ProjectGenerator {

  private final Logger log = LoggerFactory.getLogger(ProjectGeneratorImpl.class);

  private final FileProvider fileProvider;
  private final TemplateLoader templateLoader;
  private final FileSystem fileSystem;
  private final String modelDir;
  private final String defaultModel;
  private final String defaultPackage;
  private final List<ProjectGeneratorTask> tasks = new ArrayList<>();

  public ProjectGeneratorImpl(FileProvider fileProvider, TemplateLoader templateLoader, FileSystem fileSystem, String modelDir, String defaultModel, String defaultPackage) {
    this.fileProvider = fileProvider;
    this.templateLoader = templateLoader;
    this.fileSystem = fileSystem;
    this.modelDir = modelDir;
    this.defaultModel = defaultModel;
    this.defaultPackage = defaultPackage;
  }

  @Override
  public Future<Project> generate(Project project) {
    log.debug("Request to run {} tasks to generate project: {}", tasks.size(), project);
    if (project.getModel() == null || project.getModel().isEmpty()) {
      project.setModel(defaultModel);
    }
    List<Future> futures = tasks.stream().map(task -> task.execute(project)).collect(Collectors.toList());
    return CompositeFuture.all(futures)
      .compose(it -> Future.succeededFuture(project));
  }

  @Override
  public ProjectGenerator copySources() {
    return copyMainSources()
      .copyMainResources()
      .copyTestSources()
      .copyTestResources();
  }

  @Override
  public ProjectGenerator copyFile(String source, String destination) {
    requireNonNull(source);
    requireNonNull(destination);
    tasks.add(new CopyTextFile(fileProvider, fileSystem, source, destination));
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
    tasks.add(new CopySources(fileProvider, fileSystem, modelDir, SourceType.MAIN, defaultPackage));
    return this;
  }

  @Override
  public ProjectGenerator copyMainResources() {
    tasks.add(new CopyResources(fileProvider, fileSystem, modelDir, SourceType.MAIN));
    return this;
  }

  @Override
  public ProjectGenerator copyTestSources() {
    tasks.add(new CopySources(fileProvider, fileSystem, modelDir, SourceType.TEST, defaultPackage));
    return this;
  }

  @Override
  public ProjectGenerator copyTestResources() {
    tasks.add(new CopyResources(fileProvider, fileSystem, modelDir, SourceType.TEST));
    return this;
  }
}
