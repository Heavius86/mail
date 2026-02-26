package infrastructure.helpers.customelements;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class Input {
    private SelenideElement CustomInput;

    public Input(By path) {
        CustomInput = $(path);
    }

    public void sendKeys(String text) {
        CustomInput.shouldBe(visible).sendKeys(text);
    }

}
