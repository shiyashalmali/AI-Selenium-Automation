package listeners;

import base.BaseTest;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        if (!(result.getInstance() instanceof BaseTest baseTest) || baseTest.getDriver() == null) {
            System.err.println("Unable to capture failure screenshot: WebDriver is unavailable.");
            return;
        }

        WebDriver driver = baseTest.getDriver();
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Failure Screenshot", "image/png",
                    new ByteArrayInputStream(screenshot), ".png");

            Path screenshotDirectory = Path.of("screenshots");
            Files.createDirectories(screenshotDirectory);
            Path screenshotFile = screenshotDirectory.resolve(
                    result.getMethod().getMethodName() + "-" + System.currentTimeMillis() + ".png");
            Files.write(screenshotFile, screenshot);
        } catch (IOException | WebDriverException exception) {
            System.err.println("Unable to capture failure screenshot for "
                    + result.getName() + ": " + exception.getMessage());
        }
    }
}
