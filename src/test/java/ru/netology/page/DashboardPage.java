package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement headerSelector = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item div");
    private ElementsCollection goToTransferBtn = $$(".list__item button");

    public DashboardPage() {
        headerSelector.shouldBe(visible);
    }

    public int getCardBalance(int index) {
        var text = cards.get(index).text();
        return extractBalance(text);
    }

    private static int extractBalance(String text) {
        final String balanceStart = "баланс: ";
        final String balanceFinish = " р.";
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public TransferPage goToTransferPage(int index) {
        goToTransferBtn.get(index).click();
        return new TransferPage();
    }
}
