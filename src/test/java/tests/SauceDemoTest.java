package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import listeners.TestListener;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;

import java.util.List;

@Listeners(TestListener.class)
public class SauceDemoTest extends BaseTest {

    private static final String STANDARD_USERNAME = "standard_user";
    private static final String STANDARD_PASSWORD = "secret_sauce";
    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String CUSTOMER_FIRST_NAME = "Test";
    private static final String CUSTOMER_LAST_NAME = "Customer";
    private static final String CUSTOMER_POSTAL_CODE = "12345";
    private static final List<String> INVENTORY_NAMES = List.of(
            "Sauce Labs Backpack",
            "Sauce Labs Bike Light",
            "Sauce Labs Bolt T-Shirt",
            "Sauce Labs Fleece Jacket",
            "Sauce Labs Onesie",
            "Test.allTheThings() T-Shirt (Red)"
    );
    private static final List<String> INVENTORY_PRICES = List.of(
            "$29.99", "$9.99", "$15.99", "$49.99", "$7.99", "$15.99"
    );

    @Test(description = "A standard user can log in and reach the inventory page.")
    @Description("Verifies a successful login and the inventory page title.")
    public void successfulLoginOpensInventory() {
        loginPage.login(STANDARD_USERNAME, STANDARD_PASSWORD);

        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isAtInventoryPage(), "The inventory URL should be displayed.");
        Assert.assertEquals(inventoryPage.getPageTitle(), "Products");
    }

    @Test(description = "An unknown username is rejected.")
    public void invalidUsernameIsRejected() {
        loginPage.login("unknown_user", STANDARD_PASSWORD);

        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Username and password do not match any user in this service");
        Assert.assertTrue(loginPage.isLoginFormDisplayed());
    }

    @Test(description = "An incorrect password is rejected.")
    public void invalidPasswordIsRejected() {
        loginPage.login(STANDARD_USERNAME, "incorrect_password");

        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Username and password do not match any user in this service");
        Assert.assertTrue(loginPage.isLoginFormDisplayed());
    }

    @Test(description = "The locked-out account receives the expected error.")
    public void lockedOutUserCannotLogin() {
        loginPage.login("locked_out_user", STANDARD_PASSWORD);

        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Sorry, this user has been locked out.");
    }

    @Test(description = "An empty username is rejected.")
    public void emptyUsernameIsRejected() {
        loginPage.login("", STANDARD_PASSWORD);

        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username is required");
    }

    @Test(description = "An empty password is rejected.")
    public void emptyPasswordIsRejected() {
        loginPage.login(STANDARD_USERNAME, "");

        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Password is required");
    }

    @Test(description = "The inventory displays six products.")
    public void inventoryDisplaysSixProducts() {
        InventoryPage inventoryPage = loginAsStandardUser();

        Assert.assertTrue(inventoryPage.isAtInventoryPage());
        Assert.assertEquals(inventoryPage.getPageTitle(), "Products");
        Assert.assertEquals(inventoryPage.getProductCount(), 6);
    }

    @Test(description = "Inventory product names and prices match the catalog.")
    public void inventoryDisplaysProductNamesAndPrices() {
        InventoryPage inventoryPage = loginAsStandardUser();

        Assert.assertEquals(inventoryPage.getProductNames(), INVENTORY_NAMES);
        Assert.assertEquals(inventoryPage.getProductPrices(), INVENTORY_PRICES);
    }

    @DataProvider(name = "productSorts")
    public Object[][] productSorts() {
        return new Object[][]{
                {InventoryPage.SortOption.NAME_ASCENDING, INVENTORY_NAMES},
                {InventoryPage.SortOption.NAME_DESCENDING, List.of(
                        "Test.allTheThings() T-Shirt (Red)",
                        "Sauce Labs Onesie",
                        "Sauce Labs Fleece Jacket",
                        "Sauce Labs Bolt T-Shirt",
                        "Sauce Labs Bike Light",
                        "Sauce Labs Backpack")},
                {InventoryPage.SortOption.PRICE_ASCENDING,
                        List.of("$7.99", "$9.99", "$15.99", "$15.99", "$29.99", "$49.99")},
                {InventoryPage.SortOption.PRICE_DESCENDING,
                        List.of("$49.99", "$29.99", "$15.99", "$15.99", "$9.99", "$7.99")}
        };
    }

    @Test(dataProvider = "productSorts", description = "Inventory products follow the selected sort order.")
    public void productsCanBeSorted(InventoryPage.SortOption option, List<String> expectedOrder) {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.sortProductsBy(option);

        List<String> actualOrder = option == InventoryPage.SortOption.NAME_ASCENDING
                || option == InventoryPage.SortOption.NAME_DESCENDING
                ? inventoryPage.getProductNames()
                : inventoryPage.getProductPrices();
        Assert.assertEquals(actualOrder, expectedOrder, "Products should match the selected sort order.");
    }

    @Test(description = "A single selected product is added to the cart.")
    public void addSingleProductToCart() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage.addProductToCart(BACKPACK);

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1);
    }

    @Test(description = "Multiple products can be added to the cart.")
    public void addMultipleProductsToCart() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addProductToCart(BACKPACK);
        inventoryPage.addProductToCart("Sauce Labs Bike Light");

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2);
    }

    @Test(description = "The cart badge reflects the number of selected products.")
    public void cartBadgeShowsProductCount() {
        InventoryPage inventoryPage = loginAsStandardUser();
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 0, "A new cart should have no badge count.");

        inventoryPage.addProductToCart(BACKPACK);
        inventoryPage.addProductToCart("Sauce Labs Bike Light");

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2);
    }

    @Test(description = "Cart items show the expected product names, prices, and quantities.")
    public void cartDisplaysProductDetails() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addProductToCart(BACKPACK);
        inventoryPage.addProductToCart("Sauce Labs Bike Light");
        inventoryPage.openCart();
        CartPage cartPage = new CartPage(driver);

        Assert.assertEquals(cartPage.getProductNames(), List.of(BACKPACK, "Sauce Labs Bike Light"));
        Assert.assertEquals(cartPage.getProductPrices(), List.of("$29.99", "$9.99"));
        Assert.assertEquals(cartPage.getProductQuantities(), List.of("1", "1"));
    }

    @Test(description = "A selected product can be removed while other cart items remain.")
    public void removeProductFromCart() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addProductToCart(BACKPACK);
        inventoryPage.addProductToCart("Sauce Labs Bike Light");
        inventoryPage.openCart();
        CartPage cartPage = new CartPage(driver);

        cartPage.removeProduct(BACKPACK);

        Assert.assertEquals(cartPage.getProductNames(), List.of("Sauce Labs Bike Light"));
    }

    @Test(description = "The cart is empty before any product is added.")
    public void cartIsEmptyInitially() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.openCart();
        CartPage cartPage = new CartPage(driver);

        Assert.assertEquals(cartPage.getPageTitle(), "Your Cart");
        Assert.assertTrue(cartPage.isCartEmpty(), "A new cart should not contain items.");
    }

    @Test(description = "A customer can complete checkout and receive order confirmation.")
    @Description("Covers adding an item, checkout information, overview, and successful order confirmation.")
    public void completeCheckoutAndConfirmOrder() {
        CheckoutPage checkoutPage = openCheckoutWithBackpack();
        checkoutPage.submitCustomerInformation(
                CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, CUSTOMER_POSTAL_CODE);

        Assert.assertTrue(checkoutPage.isAtCheckoutOverview());
        checkoutPage.finishOrder();

        Assert.assertEquals(checkoutPage.getConfirmationHeader(), "Thank you for your order!");
    }

    @DataProvider(name = "missingCheckoutInformation")
    public Object[][] missingCheckoutInformation() {
        return new Object[][]{
                {"first name", "", CUSTOMER_LAST_NAME, CUSTOMER_POSTAL_CODE,
                        "Error: First Name is required"},
                {"last name", CUSTOMER_FIRST_NAME, "", CUSTOMER_POSTAL_CODE,
                        "Error: Last Name is required"},
                {"postal code", CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, "",
                        "Error: Postal Code is required"}
        };
    }

    @Test(dataProvider = "missingCheckoutInformation",
            description = "Checkout requires all customer information fields.")
    public void checkoutRequiresMandatoryInformation(
            String missingField, String firstName, String lastName, String postalCode, String expectedError) {
        CheckoutPage checkoutPage = openCheckoutWithBackpack();
        checkoutPage.submitCustomerInformation(firstName, lastName, postalCode);

        Assert.assertEquals(checkoutPage.getErrorMessage(),
                expectedError, "Checkout should require the " + missingField + ".");
        Assert.assertTrue(checkoutPage.isAtCheckoutInformationStep(),
                "Invalid checkout information should keep the customer on the information step.");
    }

    @Test(description = "Checkout overview displays the correct item total, tax, and grand total.")
    public void checkoutOverviewShowsCorrectTotals() {
        CheckoutPage checkoutPage = openCheckoutWithBackpack();
        checkoutPage.submitCustomerInformation(
                CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, CUSTOMER_POSTAL_CODE);

        Assert.assertTrue(checkoutPage.isAtCheckoutOverview());
        Assert.assertEquals(checkoutPage.getItemSubtotal(), "Item total: $29.99");
        Assert.assertEquals(checkoutPage.getTax(), "Tax: $2.40");
        Assert.assertEquals(checkoutPage.getGrandTotal(), "Total: $32.39");
    }

    private InventoryPage loginAsStandardUser() {
        loginPage.login(STANDARD_USERNAME, STANDARD_PASSWORD);
        return new InventoryPage(driver);
    }

    private CheckoutPage openCheckoutWithBackpack() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addProductToCart(BACKPACK);
        inventoryPage.openCart();
        new CartPage(driver).proceedToCheckout();
        return new CheckoutPage(driver);
    }

}
