package infrastructure.drivers;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.AlertNotFoundError;
import infrastructure.config.Configs;
import infrastructure.config.ChromeConfig;
import infrastructure.utils.Loggers;
import org.openqa.selenium.TimeoutException;

import java.nio.file.Paths;
import java.time.Duration;

public class CustomDriver {
    static ChromeConfig conf = Configs.getInstance();

    /**
     * Инициализация веб драйвера
     */
    public static void initDriver() {
        Configuration.savePageSource = conf.savePageSource();
        Configuration.screenshots = conf.screenshots();
        Configuration.reportsFolder = Paths.get(conf.user_dir(), "target", "tests_report").toString();
        Configuration.downloadsFolder = Paths.get(conf.user_dir(), "target", "build").toString();
        Configuration.timeout = conf.timeout();
        Configuration.browserSize = conf.browserSize();
        Configuration.pageLoadStrategy = conf.pageLoadStrategy();
        Configuration.pageLoadTimeout = conf.pageLoadTimeout();
        Configuration.reopenBrowserOnFail = conf.reopenBrowserOnFail();
        Configuration.webdriverLogsEnabled = conf.webdriverLogsEnabled();
        Configuration.textCheck = conf.textCheck();
        Configuration.browser = DriverProvider.class.getName();

        Selenide.open();
        WebDriverRunner.getAndCheckWebDriver().manage().window().maximize();
        WebDriverRunner.getAndCheckWebDriver().manage().timeouts().scriptTimeout(Duration.ofSeconds(conf.defaultDuration()));
    }

    /**
     * Проверяет создан ли драйвер в текущем потоке
     *
     * @return boolean
     */
    public static boolean isDriverRun() {
        return WebDriverRunner.hasWebDriverStarted();
    }


    public static void closeDriver(){
        WebDriverRunner.closeWebDriver();
    }
}
