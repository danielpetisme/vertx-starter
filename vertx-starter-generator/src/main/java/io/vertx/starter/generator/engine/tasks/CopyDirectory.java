package io.vertx.starter.generator.engine.tasks;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.engine.Task;
import io.vertx.starter.generator.io.FileSystem;
import io.vertx.starter.generator.model.Project;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CopyDirectory extends Task {

    private final Logger log = LoggerFactory.getLogger(CopyFile.class);

    private FileSystem fileSystem;
    private String from;
    private String into;
    private Function<String, String> rename;
    private Function<String, String> filter;


    public CopyDirectory(Project project, FileSystem fileSystem) {
        super(project);
        this.fileSystem = fileSystem;
    }

    public String from() {
        return from;
    }

    public CopyDirectory from(String from) {
        this.from = from;
        return this;
    }

    public String into() {
        return into;
    }

    public CopyDirectory into(String into) {
        this.into = into;
        return this;
    }

    public Function<String, String> filter() {
        return filter;
    }

    public CopyDirectory filter(Function<String, String> filter) {
        this.filter = filter;
        return this;
    }

    public Function<String, String> rename() {
        return rename;
    }

    public CopyDirectory rename(Function<String, String> rename) {
        this.rename = rename;
        return this;
    }

    @Override
    public Future execute() {
        log.debug("Request to copy from {} into {}", from, into);
        Path fromDir = getProject().getProjectDir().resolve(from);
        Path intoDir = getProject().getOutputDir().resolve(into);
        return files(fromDir)
            .compose(sources -> {
                List<Future> targets = new ArrayList<>();
                for (String file : sources) {
                    Path source = fromDir.resolve(file);
                    Path destination = intoDir.resolve(rename.apply(file));
                    Future<Path> future = fileSystem
                        .readFile(source)
                        .map(content -> filter.apply(content))
                        .compose(content -> fileSystem.writeFile(destination, Buffer.buffer(content)));
                    targets.add(future);
                }
                return CompositeFuture.all(targets);
            });
    }

    private Future<Set<String>> files(Path srcDir) {
        return fileSystem
            .readDir(srcDir)
            .map(filenames -> filenames
                .stream()
                .map(source -> source.replaceAll(srcDir.toString() + "/", ""))
                .collect(Collectors.toSet())
            );
    }
}
