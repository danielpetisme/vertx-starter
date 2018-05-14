package io.vertx.starter.generator.engine;

public class ProjectGeneratorConfiguration {

    public static final String DEFAULT_MODEL = "core";
    public static final String DEFAULT_PACKAGE = "io.vertx.example";
    public static final String DEFAULT_MODEL_DIR = "";
    public static final String DEFAULT_TMP_DIR = System.getProperty("java.io.tmpdir");

    private String defaultModel;
    private String defaultPackage;
    private String modelDir;
    private String tmpDir;

    public ProjectGeneratorConfiguration() {
        this.defaultModel = DEFAULT_MODEL;
        this.defaultPackage = DEFAULT_PACKAGE;
        this.modelDir = DEFAULT_MODEL_DIR;
        this.tmpDir = DEFAULT_TMP_DIR;
    }

    public String getDefaultModel() {
        return defaultModel;
    }

    public void setDefaultModel(String defaultModel) {
        this.defaultModel = defaultModel;
    }

    public String getDefaultPackage() {
        return defaultPackage;
    }

    public void setDefaultPackage(String defaultPackage) {
        this.defaultPackage = defaultPackage;
    }

    public String getModelDir() {
        return modelDir;
    }

    public void setModelDir(String modelDir) {
        this.modelDir = modelDir;
    }

    public String getTmpDir() {
        return tmpDir;
    }

    public void setTmpDir(String tmpDir) {
        this.tmpDir = tmpDir;
    }
}
