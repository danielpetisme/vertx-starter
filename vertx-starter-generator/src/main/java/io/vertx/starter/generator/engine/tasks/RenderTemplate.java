package io.vertx.starter.generator.engine.tasks;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.io.TemplateLoader;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.ProjectGeneratorTask;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class RenderTemplate implements ProjectGeneratorTask {

  private final Logger log = LoggerFactory.getLogger(RenderTemplate.class);

  private final FileSystem fileSystem;
  private final Handlebars handlebars;
  private final String template;
  private final String destination;

  public RenderTemplate(TemplateLoader templateLoader, FileSystem fileSystem, String template, String destination) {
    requireNonNull(fileSystem);
    requireNonNull(templateLoader);
    requireNonNull(template);
    requireNonNull(destination);
    this.fileSystem = fileSystem;
    this.handlebars = new Handlebars(templateLoader);
    this.template = template;
    this.destination = destination;
  }

  @Override
  public Future execute(Project project) {
    log.debug("Rendering {} to {}", template, destination);
    requireNonNull(project);
    return render(template, project)
        .compose(content -> fileSystem.writeFile(project.getOutputDir().resolve(destination), Buffer.buffer(content)));
  }

  private Future<String> render(String template, Project project) {
    Context context = Context.newBuilder(project).resolver(JavaBeanValueResolver.INSTANCE).build();
    Future future = Future.future();
    try {
      future.complete(handlebars.compile(template).apply(context));
    } catch (IOException e) {
      future.fail(e.getCause());
      log.error("Failed to render template: {} because: {}", template, e.getMessage());
    }
    return future;
  }
}
