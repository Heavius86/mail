package infrastructure.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import infrastructure.drivers.CustomDriver;


import infrastructure.utils.Constants;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;



public class AroundHooks implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) {

        try {
            Files.createDirectories(Paths.get(Constants.PATH_TO_REPORT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        if (!CustomDriver.isDriverRun()) {
            CustomDriver.initDriver();
        }
        if (!SelenideLogger.hasListener("AllureSelenide")) {
            SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                    .savePageSource(false)
                    .screenshots(true));
        }
    }

    @Override
    public void afterEach(ExtensionContext extensionContext)  {
        WebDriverRunner.closeWebDriver();
        Selenide.closeWebDriver();
    }
}
