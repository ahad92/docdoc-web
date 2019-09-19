package environment;


import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.JavascriptExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;


public class Helpers {

    public static String randomNumber(int count) {
        return RandomStringUtils.random(count, false, true);
    }

    public static String randomString(int count) {
        return RandomStringUtils.random(count, true, false);
    }

    public static void closeAllTabs() {
        open("");
        String currentTab = getWebDriver().getWindowHandle();
        Set<String> handles = getWebDriver().getWindowHandles();
        List<String> tabs = new ArrayList<String>(handles);
        for (String tab : tabs) {
            switchTo().window(tab);
            if (!getWebDriver().getWindowHandle().equals(currentTab)) {
                getWebDriver().close();
            }
        }
    }

    static void clearCookies() {
        getWebDriver().manage().deleteAllCookies();
    }

    public static void clearStorage() {
        ((JavascriptExecutor) getWebDriver()).executeScript("window.localStorage.clear();");
    }

}