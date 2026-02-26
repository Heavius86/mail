package pages.MailPages;

import infrastructure.helpers.customelements.Bottom;
import infrastructure.helpers.customelements.Input;
import org.openqa.selenium.By;

import static infrastructure.drivers.DriverActions.*;

public class LoginPage {

    private final Bottom bottom_LoginSubmit = new Bottom(By.xpath("//button[@id='rcmloginsubmit']"));
    private final Input input_Login = new Input(By.xpath("//input[@id='rcmloginuser']"));
    private final Input input_Password = new Input(By.xpath("//input[@id='rcmloginpwd']"));

    public Bottom getBottom_LoginSubmit() {
        return bottom_LoginSubmit;
    }
    public Input getInput_Login() {
        return input_Login;
    }
    public Input getInput_Password() {
        return input_Password;
    }

    public void checkOpenMailDeck(String pass) {

        checkShouldBeExist_XPath("//span[@class='header-title username' and text() = '"+pass+"']");
    }
}
