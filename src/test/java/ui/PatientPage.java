package ui;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.JavascriptExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static environment.ApiHelpers.patientRegisterAndGetApiToken;
import static environment.TestBase.PATIENT_URL;

public class PatientPage {


    private static String getPatientMainPageUrl() {
        String patientQuery = "/api/v3/consultation/?marketplace_code=MP_1&token=";
        String iframeURL = PATIENT_URL + patientQuery + patientRegisterAndGetApiToken();
        System.out.println(iframeURL);
        return iframeURL;
    }

    public void openMainPage() {
        open(getPatientMainPageUrl());
    }

    public void selectDoctor(String doctor) {
        $x("//div[text()='" + doctor + "']/..//button").click();
        sleep(1000);
    }

    public void selectPediatrician() {
        selectDoctor("Педиатр");
    }

    public void selectTherapist() {
        selectDoctor("Терапевт");
    }

    public void openMainPageInNewTab() {
        sleep(3000);
        String iframeURL = null;
        ((JavascriptExecutor) getWebDriver()).executeScript("window.open(\"" + getPatientMainPageUrl() + "\")");
        Set<String> handles = getWebDriver().getWindowHandles();
        List<String> tabs = new ArrayList<String>(handles);
        int newTab = tabs.size() - 1;
        switchTo().window(tabs.get(newTab));

    }

    public void selectPediatricianInNewTab() {
        openMainPageInNewTab();
        selectPediatrician();
    }


    public void chatIsExist() {
        $x("//div[contains(text(),'Начало консультации')]").shouldBe(exist);
    }

    public void chatIsAvailable() {
        $x("//div[contains(text(),'Начало консультации')]").shouldBe(exist);
    }

    public void loginAsPatient() {
        open("https://shop.stage2.docdoc.pro/login");
        String phoneNumber = ("9663366104");
        $("#inp-login-phone").shouldBe(visible);
        $("#inp-login-phone").setValue(phoneNumber);
        $x("//button[text()='Войти']").click();
        $("#inp-login-code").setValue("5555");
        $x("//button[contains(text(),'Подтвердить')]").click();
    }


    public void sendMessageToDoc(String message) {
        $x("//*[@id='ibolit_docdoc']//textarea").setValue(message).pressEnter();
        sleep(1000);
    }

    public void contactWithPediatrician() {
        $x("//a[contains(@href, '/product/tmk_ibolit/tmk_ibolit_telemed_therapist')]").click();
        open(PATIENT_URL + "/?token=" + "&product_id=1&specialty=pediatrician");
        sleep(5000);
        $x("//div[text()='Педиатр']/..//button").click();
    }

    public void contactWithTherapist() {
        $x("//a[contains(@href, '/product/tmk_ibolit/tmk_ibolit_telemed_therapist')]").click();
        open(PATIENT_URL + "/?token=" + "&product_id=1&specialty=therapist");
        $x("//div[text()='Терапевт']/..//button").click();
        sleep(10000);
    }

    public void acceptCallWithVideo() {

        SelenideElement selenideElement = $x("//*[@id='call-root']").waitUntil(visible, 5000);
        $x("//span[contains(text(),'С видео')]").click();
        $x("//div[contains(text(),'Видеоконсультация началась')]").shouldBe(visible);
        $("#video-undefined").shouldBe(visible);
        $("div.circle.small.expand").shouldBe(visible);

    }

    public void checkSystemMessageDoctorIsnotAvailable() {
        $x("//span[contains(text(),'Доктор пока недоступен по видеосвязи, продолжайте в чате.')]").isDisplayed();
    }


    public void acceptCallWithAudio() {
        $x("//*[@id='call-root']").shouldBe(visible);
        $x("//span[contains(text(),'Ответить')]").click();
        $x("//div[contains(text(),'Видеоконсультация началась')]").shouldBe(visible);
    }

    public void declineIncomingCall() {
        $x("//*[@id='call-root']").shouldBe(visible);
        $x("//span[contains(text(),'Отклонить')]").click();
        $x("//div[contains(text(),'Вызов отклонен')]").shouldBe(visible);
    }

    public void showSettingsOfCall() {
        $("div.circle.video").click();
        $("div.circle.video-off").click();
        $("div.circle.mic").click();
        $("div.circle.mic-off").click();
        sleep(10000);
    }

    public void checkThatFirstInQueue() {
    $("div.queue-header").shouldHave(text("Извините, пока все врачи заняты"));
    $("div.queue-sub-header").shouldHave(text("Вы первый в очереди")); }

    public void checkThatSecondInQueue() {
        $("div.queue-header").shouldHave(text("Извините, пока все врачи заняты"));
        $("div.queue-sub-header").shouldHave(text("Перед вами в очереди 1 человек"));
        sleep(2000);
    }


    public void disasbleVideoOnTheCall() {
        $("div.circle.video").click();
    }

    public void enableVideoOnTheCall() {
        $("div.circle.video-off").click();
    }

    public void disasbleMicrophoneOnTheCall() {

        $("div.circle.mic").click();
    }

    public void enableMicrophoneOnTheCall() {

        $("div.circle.mic-off").click();
    }

    public void declineVideoTalk() {
        $("div.circle.red.end").click();
    }

    public void callToDoctor() {
        $("div.profile > button").shouldBe(visible).click();
        sleep(1000);

    }
    public void sendFile() {
        File file = new File("C:\\tools\\Screenshot_58.png");
        $("input[type=file]").uploadFile(file);
        $("div.send.active").click();
    }

    public void rateConsultationWithRate5() {
        $x("//h2").shouldHave(text("Прием завершен"));
        $x("//h2/..//button[3]").click();
        $x("//h2").shouldHave(text("Пожалуйста, оставьте свой отзыв о приеме"));
        $x("$x(\"//p/../textarea['placeholder']\")\n").setValue("Заключение пациента " + "");
        $x("//p/../button").click();
        $x("//h2").shouldHave(text("Спасибо за отзыв,"));
        $x("//h2/../button").click();
        sleep(3000);
    }

    public void rateConsultationWithRate3() {

        $x("//h2").shouldHave(text("Прием завершен"));
        $x("//h2/..//button[3]").click();
        $x("//h2").shouldHave(text("Пожалуйста, оставьте свой отзыв о приеме"));
        sleep(3000);
        $x("//p/../textarea['placeholder']").setValue("Заключение пациента " + "");
        $x("//p/../button").click();
        $x("//h2").shouldHave(text("Спасибо за отзыв,"));
        $x("//h2/../button").click();
        sleep(3000);
    }


    public void rateRandom() {
        $x("//h2/..//button").waitUntil(visible,20000);
        Random rand = new Random();
        List<SelenideElement> buttons = $$x("//h2/..//button");
        int button = rand.nextInt(buttons.size());
        buttons.get(button).click();
        $x(("//p/../textarea['placeholder']")).setValue("Заключение пациента ");
        sleep(1000);
        $x("//p/../button").click();
        $x("//h2").shouldHave(text("Спасибо за отзыв,"));
        $x("//h2/../button").click();
    }


    public void checkThatConsultaionIsSuspended() {
        $x("//div[text()='Консультация приостановлена']").waitUntil(visible,40000);
        $("div.techmessage_button").shouldHave(text("Возобновить"));
    }


    public void resumeConsulatation() {
        $("div.techmessage_button").waitUntil(visible,10000);
        $("div.techmessage_button").click();
 //       $x("//div[contains(text(),'Консультация возобновлена')]").waitUntil(visible,10000);
        
    }
}




