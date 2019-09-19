package ui;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static environment.Helpers.clearStorage;
import static environment.ManagerMySQL.getPediatricianLogin;
import static environment.ManagerMySQL.getTherapistLogin;
import static environment.TestBase.DOCTOR_URL;

public class DoctorPage {

    public void openLoginPage() {
        open(DOCTOR_URL);
        clearStorage();
    }

    public void loginAsTherapist() {
        loginAs(getTherapistLogin());
    }

    public void loginAsPediatrician() {
        loginAs(getPediatricianLogin());
    }

    public void loginAs(String login) {
        $("input[name=\"login\"]").setValue(login).pressEnter();
        $("input[name=\"password\"]").setValue("qwerty").pressEnter();
    }

    protected void loginAsDoc(String docLogin, String password) {
        open("https://doctor.ibolit.stage2.docdoc.pro/");
        $(By.name("login")).setValue(docLogin).pressEnter();

        $(By.name("password")).setValue(password).pressEnter();
    }

    public void loginBySpeciality(String name) {
        switch (name) {
            case "Педиатр":
                loginAsPediatrician();
                break;
            case "Терапевт":
                loginAsTherapist();
                break;
        }
    }

    public void openMainPageInNewTab() {
        sleep(3000);

        ((JavascriptExecutor) getWebDriver()).executeScript("window.open(\"" + DOCTOR_URL + "\")");
        Set<String> handles = getWebDriver().getWindowHandles();
        List<String> tabs = new ArrayList<String>(handles);
        int newTab = tabs.size() - 1;
        switchTo().window(tabs.get(newTab));
    }

    public void sendMessageToPat() {
        $("textarea").setValue("from doctor");
        $("div.chat-field__send-icon").click();

    }

    public void sendMessagesForPatients() {

        for (int i = 1; i <= 3; i++) {
            selectChat(i);
            sendMessageToPat();
            sleep(1000);
        }
    }

    public void declineIncomingCall() {
        $x("//button[contains(text(),'Отклонить')]").click();
        $("div.profile-layout__consultations").waitUntil(visible, 10000);
    }

    public void checkSystemMessageDoctorIsnotAvailable() {
        $x("//span[contains(text(),'Доктор пока недоступен по видеосвязи, продолжайте в чате.')]").isDisplayed();

    }

    public void acceptIncomingCall() {
        $x("//button[contains(text(),'Ответить')]").click();
        sleep(4000);
    }

    public void declineVideoTalk() {
        $x("//img[contains(@alt,'Завершить')]").click();
    }

    public void disasbleVideoOnTheCall() {
        $x("//img[contains(@alt,'Камера')]").click();
    }

    public void disasbleMicrophoneOnTheCall() {
        $x("//img[contains(@alt,'Микрофон')]").click();
    }


    public void endConsultationWithConclusion() {
        $("button.button-base").click();
        $x("//button[contains(text(),'С заключением')]").click();
        $x("//div[contains(@class,'commentary-input')]//textarea").setValue("заключение");
        $x("//div[contains(@class,'commentary-input')]//button").click();
    }

    public void cancelConsultationWithFalseCall() {
        $("button.button-base").click();
        $x("//button[contains(text(),'Отмена консультации')]").click();
        $x("//label[contains(text(),'Ложный вызов')]").click();
        $x("//div[contains(@class,'endfails')]//button[contains(@class,'button-base')]").click();
    }


    public void cancelConsultationWithDisconnect() {
        $("button.button-base").click();
        $x("//button[contains(text(),'Отмена консультации')]").click();
        $x("//label[contains(text(),'Разрыв связи')]").click();
        $x("//div[contains(@class,'endfails')]//button[contains(@class,'button-base')]").click();
    }

    public void cancelConsultationWithAnother() {
        $("button.button-base").click();
        $x("//button[contains(text(),'Отмена консультации')]").click();
        $x("//label[contains(text(),'Другая причина')]").click();
        $("textarea[name=message]").setValue("some reasons");
        $x("//div[contains(@class,'endfails')]//button[contains(@class,'button-base')]").click();
    }


    public void goToMessages() {
        $x("//a[contains(@href, 'messages')]").click();
    }

    public void goToUnreadMessages() {
        $x("//a[@class ='menu__item__inner  menu__item__inner-sub']//span[text()='Непрочитанные']").click();
        //      $x("//a.chat-preview").shouldBe(visible);

    }

    public void chatlist() {
        $("div.menu__item__subnotification").click();
        List<SelenideElement> chats = $$x("a.chat-preview");
        for (int i = 1; i <= 10; i++) {
            endConsultationWithConclusion();
        }
    }

    public void goToPatients() {
        $x("//a[contains(@href, 'patients')]").click();
    }

    public void selectChat(int chat) {
        $("a.chat-preview:nth-child(" + chat + ")").click();
        sleep(3000);
    }

    public void switchSelectorToEnable() {
        $("span.header__slider.round.warning").click();
    }

    public void switchSelectorToDisable() {
        $("span.header__slider.round.false").click();
    }

    public void callToPat() {
        $("div.chat-field__attachment-icon").click();
        sleep(2000);
        $("div.chat-field-menu__item.consulation-item").click();
        $("div.webrtc__video-container__remote-video").waitUntil(visible, 10000);
//        $x("//span[contains(text(),'Видеоконсультация началась')]").waitUntil(visible,6000);
    }

    public void sendFile() {
        $("div.chat-field__attachment-icon").click();
//        $("div.chat-field-menu__item.file-item").click();
        File file = new File("C:\\tools\\Screenshot_58.png");
        $("input[type=file]").uploadFile(file);
        $("div.chat-field__send-icon").click();
    }
}