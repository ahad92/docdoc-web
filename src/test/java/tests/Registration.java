package tests;

import com.codeborne.selenide.Condition;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import environment.TestBase;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class Registration extends TestBase {
    @Test
    void loginToMPAndGoToIFrame() {

        open("https://shop.stage2.docdoc.pro/login");
        String phoneNumber = "900" + RandomStringUtils.random(7, false, true);
        $("#inp-login-phone").setValue(phoneNumber);
        $x("//button[text()='Войти']").click();
        $("#inp-login-code").setValue("5555");
        $x("//button[text()='Подтвердить']").click();
        String name = "Имя" + RandomStringUtils.random(5, true, false);
        String surname = "Фамилия" + RandomStringUtils.random(5, true, false);
        $("#inp-profile-surname").setValue(surname);
        $("#inp-profile-name").setValue(name);
        $x("//*[@id=\"js-cabinet-profile-form\"]/div/div[3]/div/div[2]/label/span[1]").click();
        $("#inp-profile-birthday").setValue("20.06.1980");
        $x("//button[text()='Завершить регистрацию']").click();

        // add promo
        $("#inp-code").setValue("TMK-IBOLIT-TEST");
        $x("//button[text()='Применить']").click();

        //check_doctor
        $x("//a[contains(@href, '/product/tmk_ibolit/tmk_ibolit_telemed_pediatrist')]").click();
   //     $x("//*[normalize-space(text()) and normalize-space(.)='Поможет с общими вопросами по здоровью ребенка'])[1]/following::button[1]").click();

        // check consultation status
        $x("//a[contains(text(),'Мои консультации')] ").click();

        //send message
        String message = "привет, что у вас болит? " + RandomStringUtils.random(50, true, false);
        $("#css=.input").shouldHave(text("visible"));
        $("#css=.input").setValue(message);
        $("#css=.send").click();


    }
}

