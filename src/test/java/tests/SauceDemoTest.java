package tests;

import base.BaseTest;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SauceDemoTest extends BaseTest {

    private static final String STANDARD_USERNAME = "standard_user";
    private static final String STANDARD_PASSWORD = "secret_sauce";
    private static final String FIRST_PRODUCT_NAME = "Sauce Labs Backpack";
    private static final String FIRST_PRODUCT_PRICE = "$29.99";

    @Test
    public void loginWithValidCredentials() {
        loginPage.enterUsername(STANDARD_USERNAME);
        loginPage.enterPassword(STANDARD_PASSWORD);
        loginPage.clickLogin();

        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(inventoryPage.isAtInventoryPage(),
                "Successful login should navigate to the inventory page.");
        Assert.assertEquals(inventoryPage.getPageTitle(), "Products");
    }

    @Test
    public void loginWithInvalidCredentials() {
        loginPage.enterUsername(STANDARD_USERNAME);
        loginPage.enterPassword("invalid_password");
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.getErrorMessage().contains(
                        "Username and password do not match any user"),
                "An authentication error should be displayed.");
        Assert.assertTrue(loginPage.isLoginFormDisplayed(),
                "The login form should remain visible after an unsuccessful login.");
    }

    @Test
    public void loginWithEmptyFields() {
        loginPage.clickLogin();

        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username is required",
                "Submitting empty fields should display the required-username error.");
        Assert.assertTrue(loginPage.isLoginFormDisplayed(),
                "The login form should remain visible after submitting empty fields.");
    }

    @Test
    public void verifyInventoryProductsAreDisplayed() {
        InventoryPage inventoryPage = loginAsStandardUser();

        Assert.assertEquals(inventoryPage.getPageTitle(), "Products");
        Assert.assertEquals(inventoryPage.getProductCount(), 6);
        Assert.assertFalse(inventoryPage.getFirstProductName().isBlank(),
                "The first inventory product should have a name.");
        Assert.assertFalse(inventoryPage.getFirstProductPrice().isBlank(),
                "The first inventory product should have a price.");
    }

    @Test
    public void addProductToCart() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage.addFirstProductToCart();

        Assert.assertEquals(inventoryPage.getFirstProductActionText(), "Remove",
                "The product action should change to Remove after adding it to the cart.");
    }

    @Test
    public void verifyCartBadgeCount() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage.addFirstProductToCart();

        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 1,
                "The cart badge should show one item.");
    }

    @Test
    public void verifyProductDetailsInCart() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addFirstProductToCart();
        inventoryPage.openCart();
        CartPage cartPage = new CartPage(driver);

        Assert.assertEquals(cartPage.getCartItemCount(), 1);
        Assert.assertEquals(cartPage.getFirstProductName(), FIRST_PRODUCT_NAME);
        Assert.assertEquals(cartPage.getFirstProductPrice(), FIRST_PRODUCT_PRICE);
        Assert.assertEquals(cartPage.getFirstProductQuantity(), "1");
    }

    @Test
    public void removeProductFromCart() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addFirstProductToCart();
        inventoryPage.openCart();
        CartPage cartPage = new CartPage(driver);

        cartPage.removeFirstProduct();

        Assert.assertEquals(cartPage.getCartItemCount(), 0,
                "The cart should be empty after removing its product.");
    }

    @Test
    public void navigateToCheckout() {
        InventoryPage inventoryPage = loginAsStandardUser();
        inventoryPage.addFirstProductToCart();
        inventoryPage.openCart();
        CartPage cartPage = new CartPage(driver);

        cartPage.proceedToCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        Assert.assertTrue(checkoutPage.isAtCheckoutInformationStep(),
                "Checkout should open the customer information step.");
        Assert.assertEquals(checkoutPage.getPageTitle(), "Checkout: Your Information");
    }

    private InventoryPage loginAsStandardUser() {
        loginPage.enterUsername(STANDARD_USERNAME);
        loginPage.enterPassword(STANDARD_PASSWORD);
        loginPage.clickLogin();
        return new InventoryPage(driver);
    }
}