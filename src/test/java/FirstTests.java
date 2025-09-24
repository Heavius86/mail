import com.codeborne.selenide.Selenide;
import infrastructure.extension.AroundHooks;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.MailPages.LoginPage;
import pages.MailPages.MailPage;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(AroundHooks.class)
public class FirstTests {

    @Test
    @DisplayName("Запустить")
    public void createTest() {
        MailPage mailPage = new MailPage();
        LoginPage loginPage = new LoginPage();

        mailPage.open("http:/roundcube/");
        loginPage.fillLoginAndPass("user@mail.com", "user@mail.com");

        loginPage.clickEnter();
        loginPage.checkOpenMailDeck("user@mail.com");

        mailPage.clickBack();
    }

    @Test
    @DisplayName("Почта")
    public void mailTest()  {
        MailPage mailPage = new MailPage();
        mailPage.open("http:/google.com/");
        assertEquals("ddd22", "ddd" + "22", "error");
    }
}
