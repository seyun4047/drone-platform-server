package com.mutzin.droneplatform.infrastructure.logging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
/// LogAppender

public class LogAppender {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//    append log front
    public static synchronized void prepend(
            String filePath,
            String message
    ) {
        try {
            Path path = Paths.get(filePath);

            // mkdir
            Files.createDirectories(path.getParent());

            String logLine = String.format(
                    "[%s] %s%n",
                    LocalDateTime.now().format(FORMATTER),
                    message
            );

            if (!Files.exists(path)) {
                Files.write(
                        path,
                        logLine.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE
                );
                return;
            }

            // read file
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            // add new log
            lines.add(0, logLine.stripTrailing());

            Files.write(
                    path,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {
            throw new IllegalStateException("ERROR: LOG APPEND", e);
        }
    }
}