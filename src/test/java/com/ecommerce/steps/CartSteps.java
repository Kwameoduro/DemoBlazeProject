package com.ecommerce.steps;

import com.ecommerce.pages.CartPage;
import com.ecommerce.pages.HomePage;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

public class CartSteps {

    private final CartPage cartPage = new CartPage();
    private final HomePage homePage = new HomePage();

    @Given("the customer adds {string} to the cart")
    public void addProductToCart(String productName) {
        homePage.clickProductByNamee(productName);
        homePage.addFirstProductToCartt();
        homePage.acceptAlertt();
    }

    @Given("the customer navigates to the Cart page")
    public void navigateToCartPage() {
        homePage.clickCartt();
        assertTrue(cartPage.isCartTableVisible(), "Cart page should be displayed");
    }

    @Then("the cart table should be visible")
    public void cartTableVisible() {
        assertTrue(cartPage.isCartTableVisible(), "Cart table should be visible");
    }

    @Then("the total price should be visible")
    public void totalPriceVisible() {
        assertTrue(cartPage.isTotalPriceFieldVisible(), "Total price should be visible");
    }

    @Then("the Place Order button should be visible")
    public void placeOrderButtonVisible() {
        assertTrue(cartPage.isPlaceOrderButtonVisible(), "Place Order button should be visible");
    }

    @When("the customer deletes the first product")
    public void deleteFirstProduct() {
        cartPage.deleteProduct(0);
    }

    @Then("the cart should be empty")
    public void cartShouldBeEmpty() {
        assertTrue(cartPage.isCartEmpty(), "Cart should be empty after deletion");
    }
}
