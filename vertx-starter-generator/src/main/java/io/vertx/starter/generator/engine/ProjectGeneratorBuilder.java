package io.vertx.starter.generator.engine;

import com.github.jknack.handlebars.io.TemplateLoader;
import io.vertx.starter.generator.io.FileSystem;

public final class ProjectGeneratorBuilder {

  private TemplateLoader templateLoader;
  private FileSystem fileSystem;
  private String modelDir;
  private String defaultModel;
  private String defaultPackage;

  public TemplateLoader getTemplateLoader() {
    return templateLoader;
  }

  public ProjectGeneratorBuilder setTemplateLoader(TemplateLoader templateLoader) {
    this.templateLoader = templateLoader;
    return this;
  }

  public FileSystem getFileSystem() {
    return fileSystem;
  }

  public ProjectGeneratorBuilder setFileSystem(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
    return this;
  }

  public String getModelDir() {
    return modelDir;
  }

  public ProjectGeneratorBuilder setModelDir(String modelDir) {
    this.modelDir = modelDir;
    return this;
  }

  public String getDefaultModel() {
    return defaultModel;
  }

  public ProjectGeneratorBuilder setDefaultModel(String defaultModel) {
    this.defaultModel = defaultModel;
    return this;
  }

  public String getDefaultPackage() {
    return defaultPackage;
  }

  public ProjectGeneratorBuilder setDefaultPackage(String defaultPackage) {
    this.defaultPackage = defaultPackage;
    return this;
  }


  public ProjectGeneratorImpl build() {
    return new ProjectGeneratorImpl(
      this.templateLoader,
      this.fileSystem,
      this.modelDir,
      this.defaultModel,
      this.defaultPackage
    );
  }

}
