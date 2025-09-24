package infrastructure.drivers;

import com.codeborne.selenide.WebDriverProvider;
import infrastructure.utils.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;

public class DriverProvider implements WebDriverProvider {

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
        chromePrefs.put("download.default_directory", Constants.PATH_TO_DEFAULT_DIR);
        options.setExperimentalOption("prefs", chromePrefs);
        options.setAcceptInsecureCerts(true);
        LoggingPreferences logPref = new LoggingPreferences();
        logPref.enable(LogType.DRIVER, Level.FINEST);
        options.setCapability("goog:loggingPrefs", logPref);


        RemoteWebDriver driver;
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

        // WebDriverManager.chromedriver().driverRepositoryUrl(url).setup();
        driver = new RemoteWebDriver(url, options);
        return driver;
    }
}
