package environment;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.LogEventListener;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.util.ResultsUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.nio.charset.StandardCharsets;
import java.util.UUID;


public class AllureSelenide implements LogEventListener {

    private final AllureLifecycle lifecycle;


    public AllureSelenide() {
        this(Allure.getLifecycle());
    }

    public AllureSelenide(final AllureLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    private static byte[] getScreenshotBytes() {
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    private static byte[] getPageSourceBytes() {
        return WebDriverRunner.getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void afterEvent(LogEvent event) {
        lifecycle.getCurrentTestCase().ifPresent(uuid -> {
            final String stepUUID = UUID.randomUUID().toString();
            lifecycle.startStep(stepUUID, new StepResult()
                    .withName(event.toString())
                    .withStatus(Status.PASSED));

            lifecycle.updateStep(stepResult -> stepResult.setStart(stepResult.getStart() - event.getDuration()));

            if (LogEvent.EventStatus.FAIL.equals(event.getStatus())) {
                lifecycle.addAttachment("Screenshot", "image/png", "png", getScreenshotBytes());
                lifecycle.addAttachment("Page source", "text/html", "html", getPageSourceBytes());
                lifecycle.updateStep(stepResult -> {
                    final StatusDetails details = ResultsUtils.getStatusDetails(event.getError())
                            .orElse(new StatusDetails());
                    stepResult.setStatus(Status.FAILED);
                    stepResult.setStatusDetails(details);
                });
            }
            lifecycle.stopStep(stepUUID);
        });
    }

    @Override
    public void beforeEvent(LogEvent currentLog) {

    }
}
