package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By pageTitle = By.className("title");
    private final By cartItems = By.className("cart_item");
    private final By productName = By.className("inventory_item_name");
    private final By productPrice = By.className("inventory_item_price");
    private final By productQuantity = By.className("cart_quantity");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
    }

    public String getPageTitle() {
        return driver.findElement(pageTitle).getText();
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public List<String> getProductNames() {
        return getCartItems().stream()
                .map(item -> item.findElement(productName).getText())
                .toList();
    }

    public List<String> getProductPrices() {
        return getCartItems().stream()
                .map(item -> item.findElement(productPrice).getText())
                .toList();
    }

    public List<String> getProductQuantities() {
        return getCartItems().stream()
                .map(item -> item.findElement(productQuantity).getText())
                .toList();
    }

    public boolean isCartEmpty() {
        return getCartItemCount() == 0;
    }

    @Step("Remove {productNameValue} from the cart")
    public void removeProduct(String productNameValue) {
        WebElement cartItem = wait.until(currentDriver -> getCartItems().stream()
                .filter(item -> item.findElement(productName).getText().equals(productNameValue))
                .findFirst()
                .orElse(null));
        WebElement removeButton = cartItem.findElement(By.cssSelector("button.cart_button"));
        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
        wait.until(currentDriver -> getCartItems().stream()
                .noneMatch(item -> item.findElement(productName).getText().equals(productNameValue)));
    }

    @Step("Proceed to checkout")
    public void proceedToCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        wait.until(ExpectedConditions.urlContains("/checkout-step-one.html"));
    }

    private List<WebElement> getCartItems() {
        return driver.findElements(cartItems);
    }
}
