package pages.MailPages;

import static infrastructure.drivers.DriverActions.*;

public class LoginPage {

    public void fillLoginAndPass(String login, String pass) {
        sendKeys_XPath("//input[@id='rcmloginuser']", login);
        sendKeys_XPath("//input[@id='rcmloginpwd']", pass);

    }

    public void clickEnter() {

        click_XPath("//button[@id='rcmloginsubmit']");
    }

    public void checkOpenMailDeck(String pass) {

        checkShouldBeExist_XPath("//span[@class='header-title username' and text() = 'user@mail.com']");
    }
}
