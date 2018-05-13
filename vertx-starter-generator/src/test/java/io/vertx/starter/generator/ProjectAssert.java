package io.vertx.starter.generator;

import io.vertx.starter.generator.model.Project;
import org.assertj.core.api.AbstractAssert;

public class ProjectAssert extends AbstractAssert<ProjectAssert, Project> {

  public ProjectAssert(Project actual) {
    super(actual, ProjectAssert.class);
  }

  public static ProjectAssert assertThat(Project actual) {
    return new ProjectAssert(actual);
  }

  public ProjectAssert hasFile(String filename) {
    isNotNull();
      if (!actual.getOutputDir().resolve(filename).toFile().exists()) {
      failWithMessage("Expected project file <%s> to exists", filename);
    }
    return this;
  }
}
