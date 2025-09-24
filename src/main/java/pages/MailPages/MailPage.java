package pages.MailPages;

import com.codeborne.selenide.Selenide;
import org.openqa.selenium.By;
import pages.BasePages.BasePage;

public class MailPage extends BasePage {
    public void clickBack() {
        Selenide.back();
        By.xpath("");
        checkAlert();
    }
}
