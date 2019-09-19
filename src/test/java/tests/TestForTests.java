package tests;

import environment.TestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.DoctorPage;
import ui.PatientPage;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static environment.ManagerMySQL.*;

public class TestForTests extends TestBase {

    private DoctorPage docPage = new DoctorPage();
    private PatientPage patPage = new PatientPage();
    private DoctorAvailabilityTests doctorAvailabilityTests = new DoctorAvailabilityTests();

    @BeforeEach
    void openPage() {
//        docPage.openLoginPage();
        connectClassDBDriver();
        setAvailableDoctor0();
    }

    @AfterEach
    void closeCons() {
        setAvailableDoctor0();
        closeAllActiveConsultations();
    }

    @Test
    void test() {
    }


    @Test
    void loginToMPAndGoToIFrame() {
        docPage.openLoginPage();
        docPage.loginAsPediatrician();
        docPage.goToMessages();
        String docTab = getWebDriver().getWindowHandle();
        patPage.openMainPageInNewTab();
        String tab1 = getWebDriver().getWindowHandle();
        patPage.selectPediatrician();
        patPage.chatIsExist();
        patPage.chatIsAvailable();
        patPage.sendMessageToDoc("sended by patient");
        patPage.callToDoctor();
        switchTo().window(docTab);
        docPage.declineIncomingCall();
        docPage.goToPatients();
        docPage.goToMessages();
        docPage.selectChat(1);
        docPage.checkSystemMessageDoctorIsnotAvailable();
        docPage.sendMessageToPat();
        docPage.callToPat();
        switchTo().window(tab1);
        patPage.checkSystemMessageDoctorIsnotAvailable();
        patPage.acceptCallWithVideo();
        sleep(5000);
        switchTo().window(docTab);
        sleep(5000);
        docPage.goToPatients();
        docPage.goToMessages();
        docPage.selectChat(1);
        docPage.sendMessageToPat();
        docPage.sendFile();
        docPage.endConsultationWithConclusion();
        switchTo().window(tab1);
        patPage.rateRandom();


    }
}