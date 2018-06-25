package io.vertx.starter;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.starter.service.model.Project;
import io.vertx.starter.service.service.ProjectGeneratorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class ProjectGeneratorServiceIntTest {

  public static final String DEFAULT_GROUP_ID = "com.acme";
  public static final String DEFAULT_ARTIFACT_ID = "example";

  Project initProject() {
    Project project = new Project();
    project.setGroupId(DEFAULT_GROUP_ID);
    project.setArtifactId(DEFAULT_ARTIFACT_ID);
    return project;
  }

  @Test
  public void testBasicMavenProject(Vertx vertx, VertxTestContext testContext) {
    ProjectGeneratorService service = new ProjectGeneratorService(vertx);
    Project project = initProject();
    service.buildProject(project).setHandler(ar -> {
      assertThat(ar.succeeded()).isEqualTo(true);
      testContext.completeNow();
    });
  }

}
