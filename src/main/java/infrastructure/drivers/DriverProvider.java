package infrastructure.drivers;

import com.codeborne.selenide.WebDriverProvider;
import infrastructure.config.Configs;
import infrastructure.config.ChromeConfig;
import infrastructure.utils.Constants;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.RemoteWebDriver;


import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;

public class DriverProvider implements WebDriverProvider {
    static ChromeConfig conf = Configs.getInstance();
    @Override
    public WebDriver createDriver(Capabilities capabilities) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--window-size=1920x1080");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-dev-shm-usage");
        // options.addArguments("--headless");
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("download.default_directory", conf.user_dir());
        options.setExperimentalOption("prefs", chromePrefs);
        options.setAcceptInsecureCerts(true);
        LoggingPreferences logPref = new LoggingPreferences();
        logPref.enable(LogType.DRIVER, Level.FINEST);
        options.setCapability("goog:loggingPrefs", logPref);


        RemoteWebDriver driver;
        URL url = conf.UrlChrome();

        driver = new RemoteWebDriver(url, options);
        return driver;
    }
}
