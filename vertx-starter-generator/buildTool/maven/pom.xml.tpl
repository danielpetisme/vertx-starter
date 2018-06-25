${vertxProject}
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>${vertxProject.groupId}</groupId>
    <artifactId>${vertxProject.artifactId}</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <properties>
        <java.version>1.8</java.version>
        <vertx.version>${vertxProject.vertxVersion}</vertx.version>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.5.1</version>
                <configuration>
                    <source>\${java.version}</source>
                    <target>\${java.version}</target>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>2.4.3</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                          <transformers>
                              <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                  <manifestEntries>
                                      <Main-Class>io.vertx.core.Launcher</Main-Class>
                                      <Main-Verticle>${vertxProject.groupId}.${vertxProject.artifactId}.MainVerticle</Main-Verticle>
                                  </manifestEntries>
                              </transformer>
                              <transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
                                  <resource>META-INF/services/io.vertx.core.spi.VerticleFactory</resource>
                                  <resource>META-INF/services/io.vertx.core.spi.launcher.CommandFactory</resource>
                              </transformer>
                          </transformers>
                          <artifactSet>
                          </artifactSet>
                          <outputFile>\${project.build.directory}/\${project.artifactId}-\${project.version}-fat.jar</outputFile>
                      </configuration>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>1.5.0</version>
                <configuration>
                    <mainClass>io.vertx.core.Launcher</mainClass>
                    <arguments>
                        <argument>run</argument>
                        <argument>${vertxProject.groupId}.${vertxProject.artifactId}.MainVerticle</argument>
                    </arguments>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <dependencies>
      <% vertxProject.dependencies.each { dependency -> %>
      <dependency>
        <groupId>io.vertx</groupId>
        <artifactId>${dependency}</artifactId>
        <version>\${vertx.version}</version>
      </dependency>
      <% } %>
    </dependencies>

</project>
