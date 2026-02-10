package infrastructure.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Loggers {
    public static final Logger CONSOLE = LogManager.getLogger("console");
    public static final Logger FILE_LOGGER = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private Loggers() {
    }
}