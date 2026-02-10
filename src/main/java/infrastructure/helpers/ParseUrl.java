package infrastructure.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUrl {
    /**
     * Регулярное выражения для разбора Url тестового стенда
     */
    private static final Pattern pattern = Pattern.compile("^(https?)://([a-zA-Z0-9]+)[.](t?s?-?elma365)[.](ru|eu)(/?)", Pattern.CASE_INSENSITIVE);

    /**
     * Проверка, что введенный URL подходит под паттерн
     *
     * @param url URL площадки против которой запущено тестирование
     * @return (boolean) true/false
     */
    public static boolean checkUrl(String url) {
        Matcher mather = pattern.matcher(url);
        return mather.find();
    }

    /**
     * Получить протокол
     *
     * @param url URL площадки против которой запущено тестирование
     * @return (String) возвращает протокол http или https
     */
    public static String getProtocol(String url) {
        Matcher mather = pattern.matcher(url);
        if (mather.find()) {
            return mather.group(1);
        }
        return null;
    }

    /**
     * Получить название компании
     *
     * @param url URL площадки против которой запущено тестирование
     * @return (String) возвращает название тестовой компании
     */
    public static String getCompanyName(String url) {
        Matcher mather = pattern.matcher(url);
        if (mather.find()) {
            return mather.group(2);
        }
        return null;
    }

    /**
     * Получить название стенда
     *
     * @param url URL площадки против которой запущено тестирование
     * @return (String) возвращает название тестового стенда
     */
    public static String getStandName(String url) {
        Matcher mather = pattern.matcher(url);
        if (mather.find()) {
            return mather.group(3);
        }
        return null;
    }

    /**
     * Получить локализацию
     *
     * @param url URL площадки против которой запущено тестирование
     * @return (String) возвращает в каком кластере расположена компани ru/eu
     */
    public static String getLocation(String url) {
        Matcher mather = pattern.matcher(url);
        if (mather.find()) {
            return mather.group(4);
        }
        return null;
    }

}
