
package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;

public class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        boolean isCi = System.getenv("CI") != null;

        switch (browser) {
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless=new", "--window-size=1920,1080");
                if (isCi) {
                    options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                }
                driver = new ChromeDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--headless=new", "--window-size=1920,1080");
                if (isCi) {
                    options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                }
                driver = new EdgeDriver(options);
            }
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: '" + browser + "'. Supported browsers are 'chrome' and 'edge'.");
        }

        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
    return driver;
}
}
