package pages.MailPages;

import pages.BasePages.BasePage;

import static infrastructure.drivers.DriverActions.back;
import static infrastructure.drivers.DriverActions.checkAlert;

public class MailPage extends BasePage {
    public void clickBack() {
        back();
        checkAlert();
    }
}
