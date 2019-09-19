package environment;

import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import java.net.MalformedURLException;

import static com.codeborne.selenide.WebDriverRunner.setWebDriver;


class DriverSetup {

    private RemoteWebDriver driver;
    private String driverPath = "C:\\tools\\chromedriver.exe";
    private String geckoPath = "c:\\tools\\geckodriver-v0.24.0-win64\\geckodriver.exe";

    void startDriver() throws MalformedURLException {
        String browser = (System.getProperty("BROWSER") == null) ? "chrome" : System.getProperty("BROWSER");
        if (browser.equals("firefox")) {
            startGeckoDriver();
        } else {
            startChromeDriver();
        }
    }

    void stopDriver() {
        WebDriverRunner.closeWebDriver();
 //       driver.close();
    }

    SessionId getSessionId() {
        return driver.getSessionId();
    }


    private void startChromeDriver() throws MalformedURLException {
        SelenideLogger.addListener("allure", new AllureSelenide());
        System.setProperty("webdriver.chrome.driver", driverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("use-fake-ui-for-media-stream");
        options.addArguments("disable-notifications");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        setWebDriver(driver);

//                URI.create("http://10.10.172.251:4444/wd/hub").toURL(), capabilities);
//        WebDriverRunner.setWebDriver(driver);
    }


    private void startGeckoDriver() throws MalformedURLException {
        SelenideLogger.addListener("allure", new AllureSelenide());
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("firefox");
        capabilities.setVersion("66.0");
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        System.setProperty("webdriver.gecko.driver", geckoPath);

//        driver = new RemoteWebDriver(
//                URI.create("http://10.10.172.251:4444/wd/hub").toURL(), capabilities);
//        WebDriverRunner.setWebDriver(driver);
    }


}
