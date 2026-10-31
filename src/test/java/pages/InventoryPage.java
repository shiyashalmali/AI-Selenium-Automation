
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.qameta.allure.Step;

import java.time.Duration;
import java.util.List;

public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By pageTitle = By.className("title");
    private final By products = By.className("inventory_item");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    public boolean isAtInventoryPage() {
        return wait.until(ExpectedConditions.urlContains("/inventory.html"));
    }

    public int getProductCount() {
        wait.until(currentDriver -> currentDriver.findElements(products).size() == 6);
        return driver.findElements(products).size();
    }

    public String getFirstProductName() {
        return getFirstProduct().findElement(By.className("inventory_item_name")).getText();
    }

    public String getFirstProductPrice() {
        return getFirstProduct().findElement(By.className("inventory_item_price")).getText();
    }

    @Step("Add the first product to the cart")
    public void addFirstProductToCart() {
        WebElement addButton = getFirstProduct().findElement(By.cssSelector("button.btn_inventory"));
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        wait.until(currentDriver ->
                "Remove".equals(getFirstProduct().findElement(By.cssSelector("button.btn_inventory")).getText()));
    }

    public String getFirstProductActionText() {
        return getFirstProduct().findElement(By.cssSelector("button.btn_inventory")).getText();
    }

    public int getCartBadgeCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge));
        return Integer.parseInt(driver.findElement(cartBadge).getText());
    }

    @Step("Open the shopping cart")
    public void openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
    }

    private WebElement getFirstProduct() {
        List<WebElement> items = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(products));
        return items.get(0);
    }
}
