package io.vertx.starter.generator.model;

import java.nio.file.Path;
import java.util.List;

public class Project {

  private String model;
  private String version;
  private Format format;
  private Language language;
  private Build build;
  private String groupId;
  private String artifactId;
  private List<String> dependencies;
  private Path baseDir;
  private Path targetDir;
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

  public Path getBaseDir() {
    return baseDir;
  }

  public void setBaseDir(Path baseDir) {
    this.baseDir = baseDir;
  }

  public Path getTargetDir() {
    return targetDir;
  }

  public void setTargetDir(Path targetDir) {
    this.targetDir = targetDir;
  }

  public String getArchivePath() {
    return archivePath;
  }

  public void setArchivePath(String archivePath) {
    this.archivePath = archivePath;
  }

  @Override
  public String toString() {
    return "Project{" +
      "model='" + model + '\'' +
      ", version='" + version + '\'' +
      ", format=" + format +
      ", language=" + language +
      ", build=" + build +
      ", groupId='" + groupId + '\'' +
      ", artifactId='" + artifactId + '\'' +
      ", dependencies=" + dependencies +
      ", baseDir=" + baseDir +
      ", targetDir=" + targetDir +
      ", archivePath='" + archivePath + '\'' +
      '}';
  }
}
