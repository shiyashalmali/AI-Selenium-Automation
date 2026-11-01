package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage {

    private final WebDriverWait wait;
    private final By pageTitle = By.className("title");
    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By itemSubtotal = By.className("summary_subtotal_label");
    private final By tax = By.className("summary_tax_label");
    private final By grandTotal = By.className("summary_total_label");
    private final By finishButton = By.id("finish");
    private final By confirmationHeader = By.className("complete-header");

    public CheckoutPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBe(pageTitle, "Checkout: Your Information"));
    }

    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    public boolean isAtCheckoutInformationStep() {
        return wait.until(ExpectedConditions.urlContains("/checkout-step-one.html"));
    }

    public boolean isAtCheckoutOverview() {
        return wait.until(ExpectedConditions.urlContains("/checkout-step-two.html"));
    }

    @Step("Submit checkout information")
    public void submitCustomerInformation(String firstNameValue, String lastNameValue, String postalCodeValue) {
        enter(firstName, firstNameValue);
        enter(lastName, lastNameValue);
        enter(postalCode, postalCodeValue);
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    public String getItemSubtotal() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(itemSubtotal)).getText();
    }

    public String getTax() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(tax)).getText();
    }

    public String getGrandTotal() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(grandTotal)).getText();
    }

    @Step("Finish the order")
    public void finishOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
        wait.until(ExpectedConditions.urlContains("/checkout-complete.html"));
    }

    public String getConfirmationHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationHeader)).getText();
    }

    private void enter(By locator, String value) {
        var field = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        field.clear();
        field.sendKeys(value);
    }
}
