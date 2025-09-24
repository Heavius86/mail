package pages.MailPages;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class LoginPage {

    public void fillLoginAndPass(String login, String pass) {
        $(By.xpath("//input[@id='rcmloginuser']")).shouldBe(visible).sendKeys(login);
        $(By.xpath("//input[@id='rcmloginpwd']")).shouldBe(visible).sendKeys(login);
    }

    public void clickEnter() {
        $(By.xpath("//button[@id='rcmloginsubmit']")).shouldBe(visible).click();
    }

    public void checkOpenMailDeck(String pass) {
        $(By.xpath("//span[@class='header-title username' and text() = 'user@mail.com']")).shouldBe(exist);
    }
}
