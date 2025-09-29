package com.ecommerce.steps;

import com.ecommerce.pages.CartPage;
import com.ecommerce.pages.HomePage;
import com.ecommerce.pages.OrderPage;
import com.ecommerce.pages.ProductPage;
import io.cucumber.java.en.*;
import org.openqa.selenium.Alert;

import static com.codeborne.selenide.Selenide.switchTo;
import static org.junit.jupiter.api.Assertions.*;

public class OrderSteps {

    private final HomePage homePage = new HomePage();
    private final CartPage cartPage = new CartPage();
    private final OrderPage orderPage = new OrderPage();
    private final ProductPage productPage = new ProductPage();


    @Given("the customer has 1 item in the cart")
    public void customerHasItemInCart() {
        homePage.clickProductByName("Samsung galaxy s6");   // opens details page
        productPage.clickAddToCartt();                       // clicks add to cart
        homePage.acceptAlert();                             // accept JS alert
        homePage.clickCart();                               // go to cart page
        assertTrue(cartPage.isCartTableVisible(), "Cart page should be visible with item inside");
    }

    @When("the customer opens the order form")
    public void openOrderForm() {
        orderPage.clickPlaceOrderFromCart();
        assertTrue(orderPage.isOrderModalDisplayed(), "Order modal should be displayed");
    }

    @When("the customer enters name {string} and credit card {string}")
    public void enterNameAndCard(String name, String cc) {
        orderPage.enterName(name);
        orderPage.enterCard(cc);
    }

    @When("the customer submits the order")
    public void submitOrder() {
        orderPage.clickPurchase();
    }

    @Then("the order confirmation should be displayed")
    public void confirmationDisplayed() {
        assertTrue(orderPage.isSuccessModalDisplayed(),
                "Success modal should be displayed after placing order");
        assertEquals("Thank you for your purchase!", orderPage.getSuccessTitle(),
                "Order success message should match");
        orderPage.clickOkButton();
    }

    @Then("an error message should be shown")
    public void errorMessageShown() {
        // Demoblaze shows a JavaScript alert when required fields are missing
        Alert alert = switchTo().alert();
        String alertText = alert.getText();
        assertTrue(alertText.toLowerCase().contains("please fill"),
                "Expected error message about missing credit card, but got: " + alertText);
        alert.accept();
    }
}
