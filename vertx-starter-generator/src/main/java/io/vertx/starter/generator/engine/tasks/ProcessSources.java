package io.vertx.starter.generator.engine.tasks;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.starter.generator.model.Project;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProcessSources extends Copy {

    private final Logger log = LoggerFactory.getLogger(CopyFile.class);

    @Override
    public Future execute(Project project) {
        Path srcDir = project.getProjectDir().resolve(project.getMainSourceSet());
        Path outputDir = project.getOutputDir().resolve(project.getMainSourceSet());
        ;
        List<Future> targets = new ArrayList<>();
        for (String file : files) {
            Path source = fromDir.resolve(file);
            Path destination = intoDir.resolve(rename.apply(file));
            Future<Path> future = fileSystem
                .readFile(source)
                .map(content -> filter.apply(content))
                .compose(content -> fileSystem.writeFile(destination, Buffer.buffer(content)));
            targets.add(future);
        }
        return CompositeFuture.all(targets);
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
