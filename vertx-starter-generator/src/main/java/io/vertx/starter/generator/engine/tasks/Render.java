package io.vertx.starter.generator.engine.tasks;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.io.TemplateLoader;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.Task;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

import java.io.IOException;

public class Render extends Task {

    private final Logger log = LoggerFactory.getLogger(Render.class);

  private final FileSystem fileSystem;
  private final Handlebars handlebars;
    private String template;
    private String destination;

    public Render(Project project, TemplateLoader templateLoader, FileSystem fileSystem) {
        super(project);
    this.fileSystem = fileSystem;
    this.handlebars = new Handlebars(templateLoader);
  }

    public String template() {
        return template;
    }

    public Render template(String template) {
        this.template = template;
        return this;
    }

    public String destination() {
        return destination;
    }

    public Render destination(String to) {
        this.destination = destination;
        return this;
    }

    @Override
    public Future execute() {
        log.debug("Rendering template {} to destination {}", template, destination);
        return render(template, getProject())
            .compose(content -> fileSystem.writeFile(getProject().getOutputDir().resolve(destination), Buffer.buffer(content)));
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
