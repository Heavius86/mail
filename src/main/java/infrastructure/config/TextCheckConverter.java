package infrastructure.config;

import com.codeborne.selenide.TextCheck;
import org.aeonbits.owner.Converter;

import java.lang.reflect.Method;

public class TextCheckConverter implements Converter<TextCheck> {
    public TextCheck convert(Method targetMethod, String text) {
        return TextCheck.valueOf(text);
    }
}
