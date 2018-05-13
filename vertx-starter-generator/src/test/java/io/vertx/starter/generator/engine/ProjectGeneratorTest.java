package io.vertx.starter.generator.engine;

import com.github.jknack.handlebars.io.TemplateLoader;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class ProjectGeneratorTest {

  private static final String MODEL_DIR = "AAAAAAAA";
  private static final String DEFAULT_MODEL = "BBBBBBBB";
  private static final String DEFAULT_PACKAGE = "CCCCCCCC";

  @Mock
  private TemplateLoader templateLoader;

  @Mock
  private FileSystem fileSystem;

  private ProjectGeneratorImpl generator;

  @BeforeEach
  public void beforeEach() {
    MockitoAnnotations.initMocks(this);
    generator = new ProjectGeneratorImpl(
      templateLoader,
      fileSystem,
      MODEL_DIR,
      DEFAULT_MODEL,
      DEFAULT_PACKAGE
    );
  }

  @Test
  public void shouldUseDefaultModelIfNotDefined(Vertx vertx, VertxTestContext testContext) {
    Project project = new Project();
    project.setModel(null);
    generator.generate(project).<Project>setHandler(ar -> {
      assertThat(ar.succeeded()).isEqualTo(true);
      assertThat(ar.result().getModel()).isEqualTo(DEFAULT_MODEL);
      testContext.completeNow();
    });
  }
}
