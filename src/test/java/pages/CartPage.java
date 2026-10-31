package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.qameta.allure.Step;

import java.time.Duration;
import java.util.List;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By pageTitle = By.className("title");
    private final By cartItems = By.className("cart_item");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public String getFirstProductName() {
        return getFirstCartItem().findElement(By.className("inventory_item_name")).getText();
    }

    public String getFirstProductPrice() {
        return getFirstCartItem().findElement(By.className("inventory_item_price")).getText();
    }

    public String getFirstProductQuantity() {
        return getFirstCartItem().findElement(By.className("cart_quantity")).getText();
    }

    @Step("Remove the first product from the cart")
    public void removeFirstProduct() {
        WebElement removeButton = getFirstCartItem().findElement(By.cssSelector("button.cart_button"));
        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
        wait.until(currentDriver -> currentDriver.findElements(cartItems).isEmpty());
    }

    @Step("Proceed to checkout")
    public void proceedToCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
    }

    private WebElement getFirstCartItem() {
        List<WebElement> items = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(cartItems));
        return items.get(0);
    }
}
