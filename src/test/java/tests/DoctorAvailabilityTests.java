package tests;

import environment.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ui.DoctorPage;
import ui.PatientPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static environment.ApiHelpers.setApiToken;

class DoctorAvailabilityTests extends TestBase {

    private PatientPage patPage = new PatientPage();
    private DoctorPage docPage = new DoctorPage();


    @BeforeEach
    void createDoctorAndPatient() {
        setApiToken();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Педиатр", "Терапевт"})
    void chooseDoctorWhenSelectorOff(String doctor) {
        docPage.openLoginPage();
        docPage.loginBySpeciality(doctor);
        patPage.openMainPageInNewTab();
        patPage.selectDoctor(doctor);
        confirm(); //close modal window
        $("#inp-login-phone").shouldBe(exist);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Педиатр", "Терапевт"})
    void chooseDoctorWhenSelectorOn(String doctor) {
        docPage.openLoginPage();
        docPage.loginBySpeciality(doctor);
        patPage.openMainPageInNewTab();
        patPage.selectDoctor(doctor);
        $("div.chat-system_msg").shouldBe(exist);
    }

    @Test
    void chooseDoctorWhenHeHaveThreeActiveConsultation() {
        docPage.openLoginPage();
        docPage.loginAsPediatrician();
        for (int i = 1; i < 4; i++) {
            patPage.selectPediatricianInNewTab();
            patPage.sendMessageToDoc("sended by patient");
            $("div.chat-system_msg").shouldBe(exist);
        }
        patPage.selectPediatricianInNewTab();
        confirm(); //close modal window
        $("#inp-login-phone").shouldBe(exist);
    }

    @Test
    void chooseDoctorAndWaitInLine() {
        docPage.openLoginPage();
        docPage.loginAsPediatrician();
        List<String> tabs = null;
        for (int i = 1; i <= 4; i++) {
            patPage.selectPediatricianInNewTab();
            patPage.sendMessageToDoc("sended by patient");
            Set<String> handles = getWebDriver().getWindowHandles();
            tabs = new ArrayList<String>(handles);
            i = tabs.size();
        }
        switchTo().window(tabs.get(0));
        docPage.goToMessages();
        docPage.sendMessagesForPatients();
        docPage.selectChat(3);
        docPage.selectChat(3);
        docPage.sendFile();
        docPage.callToPat();
        switchTo().window(tabs.get(1));
        patPage.acceptCallWithVideo();
        patPage.sendMessageToDoc("video call emulation");
        sleep(30000);
        patPage.showSettingsOfCall();
        switchTo().window(tabs.get(2));
        patPage.sendMessageToDoc("video call is not available for second and third patients");
        sleep(3000);
        switchTo().window(tabs.get(3));  // for last patient consultation should be suspended after 5 minutes
        sleep(3000);
        switchTo().window(tabs.get(1));
        patPage.sendMessageToDoc("ending of video conversation by first patient");
        sleep(3000);
        patPage.declineVideoTalk();


        while (!checkAccessibilityOfQueue()) {
            //write do doctor by patients
            for (int n = 1; n <= 3; n++) {
                patPage.sendMessageToDoc("waiting for a new slot, please wait ...");
                patPage.sendFile();
                sleep(30000);
            }
        }

        switchTo().window(tabs.get(3));
        patPage.checkThatConsultaionIsSuspended();
        patPage.selectPediatricianInNewTab();
        patPage.sendMessageToDoc("Hi Doctor!");
        switchTo().window(tabs.get(3));
        patPage.resumeConsulatation();
        patPage.checkThatFirstInQueue();
        patPage.selectPediatricianInNewTab();
        patPage.checkThatSecondInQueue();
        switchTo().window(tabs.get(0));
        docPage.goToMessages();
        docPage.selectChat(3);
        docPage.endConsultationWithConclusion();
        switchTo().window(tabs.get(1));
        patPage.rateRandom();
        switchTo().window(tabs.get(3));
        sleep(3000);
        patPage.sendMessageToDoc("Hi, finally I can ask my question!");

    }

    boolean checkAccessibilityOfQueue() {
        String firtsSendedMessageTime = $x("//div[contains(@class,'message_container message_container_TO')][1]//div[contains(@class,'time')]").getText();
        String[] time = firtsSendedMessageTime.split(" ");
        int messageTime = Integer.parseInt(time[0]);
        System.out.println("first message was written " + messageTime + " minutes ago");
        return messageTime >= 6;
    }


}






