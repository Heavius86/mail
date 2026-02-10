package infrastructure.config;

import org.aeonbits.owner.Converter;

import java.lang.reflect.Method;
import java.nio.file.Paths;

public class PathToReportConverter implements Converter<String> {
    public String convert(Method targetMethod, String text) {
        return Paths.get(System.getProperty("user.dir"), "target", "tests_report").toString();
    }
}
