package infrastructure.helpers.customelements;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class Bottom {
    private SelenideElement CustomBottom;

    public Bottom(By path) {
        CustomBottom = $(path);
    }

    public void clickBottom() {
        CustomBottom.shouldBe(visible).click();
    }

}
