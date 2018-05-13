package io.vertx.starter.generator.model;

import java.nio.file.Path;
import java.util.List;

import static java.lang.String.format;

public class Project {

    private static final String MAIN_SOURCE_SET = "src/main/%s/**%s";
    private static final String MAIN_RESOURCES = "src/main/resources";
    private static final String TEST_SOURCE_SET = "src/test/%s/**%s";
    private static final String TEST_RESOURCES = "src/test/resources";

    private String model;
    private String version;
    private Format format;
    private Language language;
    private Build build;
    private String groupId;
    private String artifactId;
    private List<String> dependencies;
    private Path projectDir;
    private Path outputDir;
    private String archivePath;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Build getBuild() {
        return build;
    }

    public void setBuild(Build build) {
        this.build = build;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public Path getProjectDir() {
        return projectDir;
    }

    public void setProjectDir(Path projectDir) {
        this.projectDir = projectDir;
    }

    public Path getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(Path outputDir) {
        this.outputDir = outputDir;
    }

    public String getArchivePath() {
        return archivePath;
    }

    public void setArchivePath(String archivePath) {
        this.archivePath = archivePath;
    }

    public String getMainSourceSet() {
        return format(MAIN_SOURCE_SET, language.getName(), language.getExtension());
    }

    public String getMainResources() {
        return MAIN_RESOURCES;
    }

    public String getTestSourceSet() {
        return format(TEST_SOURCE_SET, language.getName(), language.getExtension());
    }

    public String getTestResources() {
        return TEST_RESOURCES;
    }

}
