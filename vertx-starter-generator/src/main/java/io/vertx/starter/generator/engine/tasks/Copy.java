package io.vertx.starter.generator.engine.tasks;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.ProjectGeneratorTask;
import io.vertx.starter.generator.io.FileSystem;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Copy implements ProjectGeneratorTask {

    private final Logger log = LoggerFactory.getLogger(CopyFile.class);

    private FileSystem fileSystem;
    private Path from;
    private Path into;
    private Function<String, String> rename;
    private Function<String, String> filter;


    public Copy(FileSystem fileSystem, Path from, Path into, Function<String, String> rename, Function<String, String> filter) {
        this.fileSystem = fileSystem;
        this.from = from;
        this.into = into;
        this.rename = rename;
        this.filter = filter;
    }

    public Copy(FileSystem fileSystem, Path from, Path into) {
        this(fileSystem, from, into, Function.identity(), Function.identity());
    }

    @Override
    public Future execute() {
        log.debug("Request to copy from {} into {}", from, into);
        return files(from).compose(sources -> copy(sources));
    }

    private CompositeFuture copy(Set<String> files) {
        List<Future> targets = new ArrayList<>();
        for (String file : files) {
            Path source = from.resolve(file);
            Path destination = into.resolve(rename.apply(file));
            Future<Path> future = fileSystem
                .readFile(source)
                .map(content -> filter.apply(content))
                .compose(content -> fileSystem.writeFile(destination, Buffer.buffer(content)));
            targets.add(future);
        }
        return CompositeFuture.all(targets);
    }

//    List<Future> targets = new ArrayList<>();
//        for (String file : files) {
//        Path source = fromDir.resolve(file);
//        Path destination = intoDir.resolve(rename.apply(file));
//        Future<Path> future = fileSystem
//            .readFile(source)
//            .map(content -> filter.apply(content))
//            .compose(content -> fileSystem.writeFile(destination, Buffer.buffer(content)));
//        targets.add(future);
//    }
//        return CompositeFuture.all(targets);

    private Future<Set<String>> files(Path srcDir) {
        return fileSystem
            .readDir(srcDir)
            .map(filenames -> filenames
                .stream()
                .map(source -> source.replaceAll(srcDir.toString() + "/", ""))
                .collect(Collectors.toSet())
            );
    }

//    private Future<Path> updateSource(Path path, Function<String, String> mapper) {
//        return fileSystem.readFile(path)
//            .compose(content -> Future.succeededFuture(mapper.apply(content)))
//            .compose(content -> fileSystem.writeFile(path, Buffer.buffer(content)));
//    }
//
//    private String updatePackage(String sourceContent, String sourcePackageName, String targetPackageName) {
//        return sourceContent.replaceAll("^package " + sourcePackageName + "(.*)", "package " + targetPackageName + "$1");
//    }
//
//    public static String packageDir(String packageName) {
//        return packageName.replace("\\.", System.getProperty("path.separator"));
//    }
//
//    public static String projectPackage(Project project) {
//        return format("%s.%s", project.getGroupId(), project.getArtifactId());
//    }

}
