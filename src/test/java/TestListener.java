import base.BaseTest;

import org.testng.ITestListener;
import org.testng.ITestResult;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import io.qameta.allure.Allure;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {

        WebDriver driver = ((BaseTest) result.getInstance()).getDriver();

        File screenshot = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);

        try {
            File folder = new File("screenshots");
            folder.mkdirs();

            Files.copy(
                screenshot.toPath(),
                new File(folder, result.getName() + ".png").toPath(),
                StandardCopyOption.REPLACE_EXISTING
            );

            Allure.addAttachment(
                "Failure Screenshot",
                "image/png",
                Files.newInputStream(screenshot.toPath()),
                ".png"
            );
            System.out.println("Screenshot captured!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}