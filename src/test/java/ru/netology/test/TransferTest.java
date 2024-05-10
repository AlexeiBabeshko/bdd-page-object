package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {
    DataHelper.CardInfo firstCard;
    DataHelper.CardInfo secondCard;
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        Configuration.browserCapabilities = options;

        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCodeFor(authInfo));
        firstCard = DataHelper.getCardInfo(0, dashboardPage.getCardBalance(0));
        secondCard = DataHelper.getCardInfo(1, dashboardPage.getCardBalance(1));
    }

    @Test
    public void shouldTransferFromFirstCard() {
        var transferPage = dashboardPage.goToTransferPage(1);
        var amount = DataHelper.generateValidAmount(firstCard.getBalance());
        dashboardPage = transferPage.doValidTransfer(String.valueOf(amount), firstCard.getNumber());

        int actualFrom = dashboardPage.getCardBalance(0);
        int expectedFrom = firstCard.getBalance() - amount;

        int actualTo = dashboardPage.getCardBalance(1);
        int expectedTo = secondCard.getBalance() + amount;

        assertAll(
                () -> assertEquals(expectedFrom, actualFrom),
                () -> assertEquals(expectedTo, actualTo)
        );
    }

    @Test
    public void shouldTransferFromSecondCard() {
        var transferPage = dashboardPage.goToTransferPage(0);
        var amount = DataHelper.generateValidAmount(secondCard.getBalance());
        dashboardPage = transferPage.doValidTransfer(String.valueOf(amount), secondCard.getNumber());

        int actualFrom = dashboardPage.getCardBalance(1);
        int expectedFrom = secondCard.getBalance() - amount;

        int actualTo = dashboardPage.getCardBalance(0);
        int expectedTo = firstCard.getBalance() + amount;

        assertAll(
                () -> assertEquals(expectedFrom, actualFrom),
                () -> assertEquals(expectedTo, actualTo)
        );
    }

    @Test
    public void shouldNotTransferMoreThanBalance() {
        var transferPage = dashboardPage.goToTransferPage(0);
        var amount = secondCard.getBalance() + 1;
        dashboardPage = transferPage.doValidTransfer(String.valueOf(amount), secondCard.getNumber());

        int actualTo = dashboardPage.getCardBalance(0);
        int expectedTo = firstCard.getBalance();

        int actualFrom = dashboardPage.getCardBalance(1);
        int expectedFrom = secondCard.getBalance();

        assertAll(
                () -> assertEquals(expectedFrom, actualFrom),
                () -> assertEquals(expectedTo, actualTo)
        );
    }

    @Test
    public void shouldGetErrorWrongCardNumber() {
        var transferPage = dashboardPage.goToTransferPage(0);
        transferPage.topUpBalance("", "");
        transferPage.findErrorMsg();
    }
}
