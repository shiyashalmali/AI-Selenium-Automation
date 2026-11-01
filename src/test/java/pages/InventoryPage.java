package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By pageTitle = By.className("title");
    private final By products = By.className("inventory_item");
    private final By productNames = By.className("inventory_item_name");
    private final By productPrices = By.className("inventory_item_price");
    private final By productSort = By.cssSelector("[data-test='product-sort-container']");
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
        return wait.until(currentDriver -> {
            List<WebElement> items = currentDriver.findElements(products);
            return items.size() == 6 ? items.size() : null;
        });
    }

    public List<String> getProductNames() {
        return driver.findElements(productNames).stream().map(WebElement::getText).toList();
    }

    public List<String> getProductPrices() {
        return driver.findElements(productPrices).stream().map(WebElement::getText).toList();
    }

    @Step("Sort products by {option}")
    public void sortProductsBy(SortOption option) {
        new Select(wait.until(ExpectedConditions.elementToBeClickable(productSort)))
                .selectByValue(option.value);
    }

    @Step("Add {productName} to the cart")
    public void addProductToCart(String productName) {
        WebElement product = getProduct(productName);
        WebElement addButton = product.findElement(By.cssSelector("button.btn_inventory"));
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        wait.until(currentDriver ->
                "Remove".equals(getProduct(productName)
                        .findElement(By.cssSelector("button.btn_inventory")).getText()));
    }

    @Step("Remove {productName} from the inventory cart")
    public void removeProductFromCart(String productName) {
        WebElement product = getProduct(productName);
        WebElement removeButton = product.findElement(By.cssSelector("button.btn_inventory"));
        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
        wait.until(currentDriver ->
                "Add to cart".equals(getProduct(productName)
                        .findElement(By.cssSelector("button.btn_inventory")).getText()));
    }

    public int getCartBadgeCount() {
        List<WebElement> badges = driver.findElements(cartBadge);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());
    }

    @Step("Open the shopping cart")
    public void openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
    }

    private WebElement getProduct(String productName) {
        return wait.until(currentDriver -> currentDriver.findElements(products).stream()
                .filter(item -> item.findElement(productNames).getText().equals(productName))
                .findFirst()
                .orElse(null));
    }

    public enum SortOption {
        NAME_ASCENDING("az"),
        NAME_DESCENDING("za"),
        PRICE_ASCENDING("lohi"),
        PRICE_DESCENDING("hilo");

        private final String value;

        SortOption(String value) {
            this.value = value;
        }
    }
}
