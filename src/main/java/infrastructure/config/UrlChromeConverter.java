package infrastructure.config;

import org.aeonbits.owner.Converter;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class UrlChromeConverter implements Converter<URL> {
    public URL convert(Method targetMethod, String text) {
        URL url;
        try {
            String value = System.getProperty("is_docker");
            if (Objects.nonNull(value) && value.equals("true"))
                url = new URL("http://chrome:4444/wd/hub");
            else
                url = new URL("http://localhost:4444/wd/hub");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return url;
    }
}
