import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AndroidTestAppium {
    AndroidDriver<AndroidElement> driver;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("browserstack.user", "marynakrasiuk_byFA1r");
        caps.setCapability("browserstack.key", "48qgNnf7ns4ZqG9R4pDS");
        caps.setCapability("app", "bs://c700ce60cf13ae8ed97705a55b8e022f13c5827c");
        caps.setCapability("device", "Samsung Galaxy S9");
        caps.setCapability("os_version", "8.0");

        caps.setCapability("project", "First Java Project");
        caps.setCapability("build", "browserstack-build-1");
        caps.setCapability("name", "first_test");

        driver = new AndroidDriver(new URL("http://hub.browserstack.com/wd/hub"), caps);

    }

    @Test
    public void android_test_appium() {
        AndroidElement searchElement = findElementByWithWait(MobileBy.AccessibilityId("Search Wikipedia"));
        searchElement.click();
        AndroidElement insertTextElement = findElementByWithWait(MobileBy.id("org.wikipedia.alpha:id/search_src_text"));
        insertTextElement.sendKeys("BrowserStack");

        List<AndroidElement> allProductsName = findListOfElementsByWithWait(By.className("android.widget.TextView"));
        assert(allProductsName.size() > 0);
    }

    @AfterEach
    public void cleanUp() {
        driver.quit();
    }
    private ExpectedCondition<List<AndroidElement>> elementsAreDisplayed(By by) {
        return driver -> {
            List<AndroidElement> listMobileElemets;
            listMobileElemets = driver.findElements(by);
            if (listMobileElemets.size() > 0 && listMobileElemets.get(0).isDisplayed()) {
                return listMobileElemets;
            }else return null;
        };
    }

    protected List<AndroidElement> findListOfElementsByWithWait(By by) {
        return new WebDriverWait(driver, 10).until(elementsAreDisplayed(by));
    }

    private ExpectedCondition<AndroidElement> elementIsDisplayed(By by) {
        return driver -> {
            List<AndroidElement> listMobileElemets;
            listMobileElemets = driver.findElements(by);
            if (listMobileElemets.size() > 0 && listMobileElemets.get(0).isDisplayed()) {
                return listMobileElemets.get(0);
            }else return null;
        };
    }

    protected AndroidElement findElementByWithWait(By by) {
        return new WebDriverWait(driver, 10).until(elementIsDisplayed(by));
    }
}
