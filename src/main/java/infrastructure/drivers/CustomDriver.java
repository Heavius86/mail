package infrastructure.drivers;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.TextCheck;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.AlertNotFoundError;
import infrastructure.helpers.Backend;
import infrastructure.helpers.TestConfig;
import infrastructure.utils.Constants;
import infrastructure.utils.Loggers;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class CustomDriver {
    private static TestConfig config = TestConfig.getInstance();

    /**
     * Инициализация веб драйвера
     */
    public static void initDriver() {
        Configuration.savePageSource = false;
        Configuration.screenshots = false;
        Configuration.reportsFolder = Constants.PATH_TO_REPORT;
        Configuration.downloadsFolder = Paths.get(Constants.PATH_TO_DEFAULT_DIR, "target", "build").toString();
        Configuration.timeout = 15000;
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "normal";
        Configuration.pageLoadTimeout = 60000;
        Configuration.reopenBrowserOnFail = true;
        Configuration.webdriverLogsEnabled = true;
        Configuration.textCheck = TextCheck.PARTIAL_TEXT;
        Configuration.browser = DriverProvider.class.getName();

        Selenide.open();
        WebDriverRunner.getAndCheckWebDriver().manage().window().maximize();
        WebDriverRunner.getAndCheckWebDriver().manage().timeouts().scriptTimeout(Duration.ofSeconds(60));
    }

    /**
     * Проверяет создан ли драйвер в текущем потоке
     *
     * @return boolean
     */
    public static boolean isDriverRun() {
        return WebDriverRunner.hasWebDriverStarted();
    }

    /**
     * Останавливает поток выполнения на указанное время
     *
     * @param mills миллисекунды
     */
    public static void waitMills(long mills) {
        Selenide.sleep(mills);
    }

    /**
     * Устанавливаем cookie администратора
     */
    public static void setCookieAdmin() {
        String token = Backend.getAuthTokenAdmin();
        Assertions.assertNotNull(token, "Токен не м.б NULL");
        Assertions.assertNotEquals(token, "", "Пустой токен");
        Assertions.assertNotEquals(isDriverRun(), false, "Не запустился драйвер");
        Selenide.clearBrowserCookies();
        setItemCookieToken(token);
    }

    /**
     * Устанавливаем cookie пользователя
     */
    public static void setCookieUser() {
        String token = Backend.getAuthTokenUser();
        Assertions.assertNotNull(token, "Токен не м.б NULL");
        Assertions.assertNotEquals(token, "", "Пустой токен");
        Assertions.assertNotEquals(isDriverRun(), false, "Не запустился драйвер");
        Selenide.clearBrowserCookies();
        setItemCookieToken(token);
    }

    /**
     * Установить куки авторизации
     *
     * @param token токен авторизации
     */
    private static void setItemCookieToken(final String token) {
        Cookie cookie = new Cookie("vtoken", token);
        WebDriverRunner.getAndCheckWebDriver().manage().addCookie(cookie);
    }

    /**
     * Переключиться на указанный frame
     *
     * @param frame String название fraime
     */
    public static void switchToFrame(String frame) {
        Selenide.switchTo().frame(frame);
    }

    /**
     * Получить текущий url
     *
     * @return String текущий url
     */
    public static String getCurrentUrl() {
        return WebDriverRunner.url();
    }

    /**
     * Переключиться на основной контент страницы
     */
    public static void switchToDefaultContent() {
        Selenide.switchTo().defaultContent();
    }

    /**
     * Закрыть alert JS "Подтвердить"
     * Немного костыльный метод, вызывается каждый раз после открытия страницы
     * Так же делает задержку после открытия в 1000мс
     */
    public static void alertAccept() {
        try {
            Selenide.switchTo().alert(Duration.ofMillis(1000)).accept();
            Loggers.CONSOLE.warn("Close JS alert Accept");
        } catch (TimeoutException | AlertNotFoundError ignored) {
        }
    }

    /**
     * Сделать скриншот, скриншот будет сохранен в текущую директорию
     * в папку screenshot
     *
     * @param testName Название скриншота
     * @return Имя файла
     */
    public static String makeScreenshot(String testName) {
        return isDriverRun() ? Selenide.screenshot(testName) : null;
    }

    /**
     * Получить логи
     *
     * @param logType тип логов
     * @return List<LogEntry> указанные логи
     */
    public static List<LogEntry> getLogs(String logType) {
        if (isDriverRun()) {
            return WebDriverRunner.getAndCheckWebDriver().manage().logs().get(logType).getAll();
        }
        return null;
    }

    /**
     * Получить Actions
     *
     * @return Actionss
     */
    public static Actions getAction() {
        return Selenide.actions();
    }

    /**
     * Очисщаем куки браузера
     */
    public static void clearAllCookie(){
        Selenide.clearBrowserCookies();
    }

    /**
     * Очищаем куки и локалсторейдж браузера.
     * Использовать при смене ролей обязательно,
     * так как хранимые скрипты влияют на работу даже после очистки куков
     */
    public static void clearStoredData(){
        Selenide.clearBrowserCookies();
        Selenide.localStorage().clear();
        Selenide.sessionStorage().clear();
        closeDriver();
        initDriver();

    }

    public static void closeDriver(){
        WebDriverRunner.closeWebDriver();
    }
}
