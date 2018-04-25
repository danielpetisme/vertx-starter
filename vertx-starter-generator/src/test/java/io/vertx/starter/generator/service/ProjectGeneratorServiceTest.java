package io.vertx.starter.generator.service;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.starter.generator.engine.ProjectGenerator;
import io.vertx.starter.generator.engine.SourceType;
import io.vertx.starter.generator.model.Build;
import io.vertx.starter.generator.model.Language;
import io.vertx.starter.generator.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static io.vertx.starter.generator.ProjectAssert.assertThat;
import static io.vertx.starter.generator.util.ProjectUtil.packageDir;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class ProjectGeneratorServiceTest {

  private final Logger log = LoggerFactory.getLogger(ProjectGeneratorServiceTest.class);


  public static final String DEFAULT_GROUP_ID = "com.acme";
  public static final String DEFAULT_ARTIFACT_ID = "example";

  private static final String PROJECTS_DIR = "src/main/resources/projects";

  private Vertx vertx = Vertx.vertx();

  private ProjectGeneratorService service;

  @BeforeEach
  public void beforeEach() {
    service = new ProjectGeneratorService(ProjectGenerator.defaultProjectGenerator(vertx));
  }

  Project initProject() {
    Project project = new Project();
    project.setGroupId(DEFAULT_GROUP_ID);
    project.setArtifactId(DEFAULT_ARTIFACT_ID);
    String rootDir = System.getProperty("java.io.tmpdir");
    Path baseDir = Paths.get(rootDir).resolve(UUID.randomUUID().toString());
    project.setBaseDir(baseDir);
    project.setTargetDir(baseDir.resolve(project.getArtifactId()));
    return project;
  }

  @ParameterizedTest
  @EnumSource(Language.class)
  public void testBasicMavenProject(Language language, Vertx vertx, VertxTestContext testContext) {
    Project project = initProject();
    project.setBuild(Build.MAVEN);
    project.setLanguage(language);
    generateAndVerify(project, testContext, () -> {
      try {
        verifyProject(project);
      } catch (Exception e) {
        testContext.failNow(e);
      }
    });
  }

  @ParameterizedTest
  @EnumSource(Language.class)
  public void testBasicGradleProject(Language language, Vertx vertx, VertxTestContext testContext) {
    Project project = initProject();
    project.setBuild(Build.GRADLE);
    project.setLanguage(language);
    generateAndVerify(project, testContext, () -> {
      try {
        verifyProject(project);
      } catch (Exception e) {
        testContext.failNow(e);
      }
    });
  }

  private void generateAndVerify(Project project, VertxTestContext testContext, Runnable tests) {
    service.generate(project).setHandler(testContext.succeeding(ar -> {
      try {
        testContext.verify(tests);
        testContext.completeNow();
      } catch (Exception e) {
        testContext.failNow(e);
      }
    }));
  }

  public void verifyProject(Project project) throws Exception {
    verifyBasic(project);
    if (project.getBuild() == Build.MAVEN) {
      verifyMaven(project);
    }
    if (project.getBuild() == Build.GRADLE) {
      verifyGradle(project);
    }
    verifyProjectSources(project);
  }

  public void verifyBasic(Project project) {
    assertThat(project).hasFile(".gitignore");
    assertThat(project).hasFile(".editorconfig");
  }

  public void verifyMaven(Project project) {
    assertThat(project).hasFile(".mvn/wrapper/maven-wrapper.jar");
    assertThat(project).hasFile(".mvn/wrapper/maven-wrapper.properties");
    assertThat(project).hasFile("mvnw");
    assertThat(project).hasFile("mvnw.bat");
    assertThat(project).hasFile(".mvn/wrapper/maven-wrapper.jar");
    assertThat(project).hasFile("pom.xml");
  }

  public void verifyGradle(Project project) {
    assertThat(project).hasFile("gradle/wrapper/gradle-wrapper.jar");
    assertThat(project).hasFile("gradle/wrapper/gradle-wrapper.properties");
    assertThat(project).hasFile("gradlew");
    assertThat(project).hasFile("gradlew.bat");
    assertThat(project).hasFile("settings.gradle");
    assertThat(project).hasFile("build.gradle");
  }

  public void verifyProjectSources(Project project) throws Exception {
    Path modelDir = Paths.get(PROJECTS_DIR).resolve(project.getModel());
    verifySources(project, modelDir, SourceType.MAIN);
    verifyResources(project, modelDir, SourceType.MAIN);
    verifySources(project, modelDir, SourceType.TEST);
    verifyResources(project, modelDir, SourceType.TEST);
  }

  public void verifySources(Project project, Path modelDir, SourceType sourceType) throws Exception {
    Path src = modelDir.resolve("src").resolve(sourceType.getName()).resolve(project.getLanguage().getName());
    Files.walk(src)
      .filter(p -> Files.isRegularFile(p))
      .map(p -> p.toString().replaceAll(modelDir.toString() + "/", ""))
      .map(filename -> filename.replaceAll("io/vertx/example", packageDir(project)))
      .forEach(filename -> {
        assertThat(project).hasFile(filename);
      });
  }

  public void verifyResources(Project project, Path modelDir, SourceType sourceType) throws Exception {
    Path resources = modelDir.resolve("src").resolve(sourceType.getName()).resolve("resources");
    if (resources.toFile().exists()) {
      Files.walk(resources)
        .filter(p -> Files.isRegularFile(p))
        .map(p -> p.toString().replaceAll(modelDir.toString() + "/", ""))
        .forEach(filename -> {
          assertThat(project).hasFile(filename);
        });
    }
  }

}
