package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginSelector = $("[data-test-id=login] input");
    private SelenideElement passwordSelector = $("[data-test-id=password] input");
    private SelenideElement sendButton = $("[data-test-id=action-login]");

    public VerificationPage validLogin(DataHelper.AuthInfo info){
        loginSelector.setValue(info.getLogin());
        passwordSelector.setValue(info.getPassword());
        sendButton.click();
        return new VerificationPage();
    }

}
