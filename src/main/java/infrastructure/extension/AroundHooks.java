package infrastructure.extension;

import com.codeborne.selenide.WebDriverRunner;
import infrastructure.config.ChromeConfig;
import infrastructure.config.Configs;
import infrastructure.drivers.CustomDriver;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static infrastructure.drivers.DriverActions.addWebDriverListener;
import static infrastructure.drivers.DriverActions.closeWebDriver;


public class AroundHooks implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback {
    static ChromeConfig conf = Configs.getInstance();

    @Override
    public void beforeAll(ExtensionContext extensionContext) {

        try {
            Files.createDirectories(Paths.get(conf.path_to_report()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        if (!CustomDriver.isDriverRun()) {
            CustomDriver.initDriver();
        }

        addWebDriverListener();
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        WebDriverRunner.closeWebDriver();
        closeWebDriver();
    }
}
