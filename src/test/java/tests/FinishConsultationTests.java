package tests;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ui.PatientPage;

import static com.codeborne.selenide.Selenide.*;
import static environment.ApiHelpers.setApiToken;

public class FinishConsultationTests {

    private PatientPage patMainPage = new PatientPage();

    @BeforeEach
    void patientRegister() {
        setApiToken();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Педиатр", "Терапевт"})
    void selectDoctorWhenHeIsNotOnline(String doctor) {
        patMainPage.openMainPage();
        patMainPage.selectDoctor(doctor);
        confirm(); //close modal window
        $("#inp-login-phone").shouldBe(Condition.exist);
    }
}
