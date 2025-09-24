package infrastructure.utils;

import java.nio.file.Paths;

public final class Constants {
    public static final String PATH_TO_REPORT = Paths.get(System.getProperty("user.dir"), "target", "tests_report").toString();
    public static final String PATH_TO_DEFAULT_DIR = Paths.get(System.getProperty("user.dir")).toString();

    private Constants() {}
}