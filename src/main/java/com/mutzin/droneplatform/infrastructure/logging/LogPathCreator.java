package com.mutzin.droneplatform.infrastructure.logging;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/// LogPathCreator

public class LogPathCreator {
    private static final DateTimeFormatter DATE =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Path createAppLog() {
        return create("logs", "app", true);
    }

    public static Path create(String dir, String name, boolean daily) {

        String fileName = daily
                ? name + "-" + LocalDate.now().format(DATE) + ".log"
                : name + ".log";

        return Paths.get(dir, fileName);
    }


//     logs/auth/auth-2026-01-31.log
    public static Path createWithSubDir(
            String rootDir,
            String subDir,
            String fileName
    ) {
        return Paths.get(rootDir, subDir, fileName + ".log");
    }
}
