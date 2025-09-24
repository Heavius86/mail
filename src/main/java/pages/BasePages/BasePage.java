package pages.BasePages;

import com.codeborne.selenide.Selenide;
import infrastructure.drivers.CustomDriver;
import infrastructure.extension.AroundHooks;
import infrastructure.helpers.TestConfig;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.ExtendWith;


import java.util.Locale;


public abstract class BasePage {
    protected static TestConfig config = TestConfig.getInstance();

    /**
     * Открыть страницу
     *
     * @param fullUrl путь до ресурса
     */
    public void open(String fullUrl) {

        Selenide.open(fullUrl);

        checkAlert();

    }

    /**
     * Обновить страницу
     */
    public void refreshPage() {
        Selenide.refresh();
        checkAlert();
    }

    /**
     * Нажать кнопку назад (действие браузера)
     */
    public void clickBack() {
        Selenide.back();
        checkAlert();
    }



    /**
     * Проверяем что нет никаких JS алертов и ждем когда перестанут грузится спиннеры
     */
    protected void checkAlert() {
        // доступ изменён на protected, что бы наследники могли вызвать метод.
        CustomDriver.alertAccept();
    }

}