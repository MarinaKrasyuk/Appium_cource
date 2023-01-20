import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSElement;
import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.util.List;


public class IosTestAppium extends IOSBaseTest {
    @ParameterizedTest
    @MethodSource("devices")
    public void ios_test_appium(int taskId) throws MalformedURLException {
        createConnection(taskId);

        IOSElement textButton = findElementByWithWait(MobileBy.AccessibilityId("Text Button"));
        textButton.click();
        IOSElement textInput = findElementByWithWait(MobileBy.AccessibilityId("Text Input"));
        textInput.sendKeys("hello@browserstack.com"+"\n");

        IOSElement textOutput = findElementByWithWait(MobileBy.AccessibilityId("Text Output"));

        Assert.assertEquals(textOutput.getText(),"hello@browserstack.com");
    }
    
    private ExpectedCondition<IOSElement> elementsAreDisplayed(By by) {
        return driver -> {
            List<IOSElement> listMobileElemets;
            listMobileElemets = driver.findElements(by);
            if (listMobileElemets.size() > 0 && listMobileElemets.get(0).isDisplayed()) {
                return listMobileElemets.get(0);
            }else return null;
        };
    }

    protected IOSElement findElementByWithWait(By by) {
        return new WebDriverWait(driver, 10).until(elementsAreDisplayed(by));
    }
}
