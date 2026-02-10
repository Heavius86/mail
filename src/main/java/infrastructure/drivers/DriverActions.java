package infrastructure.drivers;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.ex.AlertNotFoundError;
import com.codeborne.selenide.logevents.SelenideLogger;
import infrastructure.utils.Loggers;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DriverActions {
    /**
     * Открываем страницу
     *
     * @param fullUrl
     */
    public static void openPage(String fullUrl) {
        Selenide.open(fullUrl);
    }

    /**
     * Проверяем что нет никаких JS алертов и ждем когда перестанут грузится спиннеры
     */
    public static void checkAlert() {
        try {
            Selenide.switchTo().alert(Duration.ofMillis(1000)).accept();
            Loggers.CONSOLE.warn("Close JS alert Accept");
        } catch (TimeoutException | AlertNotFoundError ignored) {
            System.out.println("Ошибка при работе с алертом");
        }
    }

    /**
     * Останавливает поток выполнения на указанное время
     *
     * @param mills миллисекунды
     */
    public static void waitMills(long mills) {
        Selenide.sleep(mills);
    }


    public static void refresh() {
        Selenide.refresh();
    }

    public static void back() {
        Selenide.back();
    }

    public static void sendKeys_XPath(String path, String text) {
        $(By.xpath(path)).shouldBe(visible).sendKeys(text);
    }

    public static void click_XPath(String path) {
        $(By.xpath(path)).shouldBe(visible).click();
    }

    public static void checkShouldBeExist_XPath(String path) {
        $(By.xpath("//span[@class='header-title username' and text() = 'user@mail.com']")).shouldBe(exist);
    }

    public static void closeWebDriver() {
        Selenide.closeWebDriver();
    }

    public static void addWebDriverListener() {
        if (!SelenideLogger.hasListener("AllureSelenide")) {
            SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                    .savePageSource(false)
                    .screenshots(true));
        }
    }
}
