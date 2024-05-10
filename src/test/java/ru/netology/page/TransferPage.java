package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amountSelector = $("[data-test-id=amount] input");
    private SelenideElement cardNumberSelector = $("[data-test-id=from] input");
    private SelenideElement transferBtn = $("[data-test-id=action-transfer]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");
    public TransferPage() {
        amountSelector.shouldBe(visible);
    }

    public void topUpBalance(String amount, String cardNumberFrom) {
        amountSelector.doubleClick().sendKeys(Keys.BACK_SPACE);
        cardNumberSelector.doubleClick().sendKeys(Keys.BACK_SPACE);
        amountSelector.setValue(amount);
        cardNumberSelector.setValue(cardNumberFrom);
        transferBtn.click();
    }

    public DashboardPage doValidTransfer(String amount, String cardNumberFrom) {
        topUpBalance(amount, cardNumberFrom);
        return new DashboardPage();
    }

    public void findErrorMsg(){
        errorNotification.shouldBe(visible);
    }
}
