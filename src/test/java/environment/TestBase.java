package environment;

import io.qameta.allure.model.Link;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.net.MalformedURLException;

import static environment.ManagerMySQL.connectClassDBDriver;

//import static environment.ApiHelpers.patientRegisterAndSetApiToken;


public class TestBase {

    private static DriverSetup driver = new DriverSetup();
    private Link mylink = new Link();
    final static String API_URL = "https://api.ibolit.amedzhidov.docdoc.pro";
    public final static String PATIENT_URL = "https://patient.ibolit.amedzhidov.docdoc.pro";
    public final static String DOCTOR_URL = "https://doctor.ibolit.amedzhidov.docdoc.pro";

    @BeforeAll
    static void init() throws MalformedURLException {
        connectClassDBDriver();
        driver.startDriver();

    }

    @AfterAll
    static void tearDown() {
        driver.stopDriver();


    }

//    @Test
//    boolean isElementPresent(By locator) {
//        try {
//            locator;
//
//            $x("//select[@name='options[Size]']").shouldHave(visible);
//            return true;
//        } catch (NoSuchElementException ex) {
//            return false;
//        }
//    }

//    @AfterEach
//    void addVideoLinkToReport() {
//        addLinks(addVideoLink());
//        closeAllTabs();
//        clearCookies();
//    }

//    private Link addVideoLink() {
//        mylink.setName("CLICK_TO_WATCH_THE_VIDEO");
//        mylink.setUrl("http://10.10.172.251:4444/video/" + driver.getSessionId() + ".mp4");
//        mylink.setType("video");
//        return mylink;
//    }

}
