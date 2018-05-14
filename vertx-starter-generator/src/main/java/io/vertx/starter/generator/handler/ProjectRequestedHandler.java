package io.vertx.starter.generator.handler;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.ProjectGenerator;
import io.vertx.starter.generator.model.Build;
import io.vertx.starter.generator.model.Project;

public class ProjectRequestedHandler {

    private final Logger log = LoggerFactory.getLogger(ProjectRequestedHandler.class);

    private final ProjectGenerator projectGenerator;

    public ProjectRequestedHandler(ProjectGenerator projectGenerator) {
        this.projectGenerator = projectGenerator;
    }

    public void handle(Message<JsonObject> message) {
        Project project = message.body().mapTo(Project.class);
        projectGenerator
            .render("_gitignore", ".gitignore")
            .copyFile("_editorconfig", ".editorconfig");
        if (project.getBuild() == Build.MAVEN) {
            projectGenerator
                .copyFile("_mvn/wrapper/maven-wrapper.jar", ".mvn/wrapper/maven-wrapper.jar")
                .copyFile("_mvn/wrapper/maven-wrapper.properties", ".mvn/wrapper/maven-wrapper.properties")
                .copyFile("mvnw", "mvnw")
                .copyFile("mvnw.bat", "mvnw.bat")
                .render("pom.xml", "pom.xml");
        }
        if (project.getBuild() == Build.GRADLE) {
            projectGenerator
                .copyFile("gradle/wrapper/gradle-wrapper.jar", "gradle/wrapper/gradle-wrapper.jar")
                .copyFile("gradle/wrapper/gradle-wrapper.properties", "gradle/wrapper/gradle-wrapper.properties")
                .copyFile("gradlew", "gradlew")
                .copyFile("gradlew.bat", "gradlew.bat")
                .render("build.gradle", "build.gradle")
                .render("settings.gradle", "settings.gradle");
        }

        projectGenerator
            .processMainSources()
            .processTestSources()
            .generate(project);
    }

}
