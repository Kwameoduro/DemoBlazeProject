package com.ecommerce.steps;

import com.ecommerce.pages.OrderPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrderSteps {
    private final OrderPage orderPage = new OrderPage();

    @Given("the customer clicks Place Order")
    public void clickPlaceOrder() {
        assertTrue(orderPage.isOrderModalDisplayed(),
                "Order modal should be visible after clicking Place Order");
    }

    @When("the customer enters order details")
    public void enterOrderDetails(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        orderPage.enterName(data.get("name"));
        orderPage.enterCountry(data.get("country"));
        orderPage.enterCity(data.get("city"));
        orderPage.enterCard(data.get("card"));
        orderPage.enterMonth(data.get("month"));
        orderPage.enterYear(data.get("year"));
    }

    @When("the customer clicks Purchase")
    public void clickPurchase() {
        orderPage.clickPurchase();
    }

    @Then("the success modal should be displayed")
    public void successModalDisplayed() {
        assertTrue(orderPage.isSuccessModalDisplayed(),
                "Success modal should be displayed after purchase");
    }

    @Then("the success title should be {string}")
    public void verifySuccessTitle(String expected) {
        assertEquals(expected, orderPage.getSuccessTitle(),
                "Success modal title should match");
    }

    @Then("the success details should not contain the card details {string}")
    public void verifyCardNotDisplayed(String cardNumber) {
        String details = orderPage.getSuccessDetails();
        assertFalse(details.contains(cardNumber),
                "Card number should not appear in success details!");
    }

    @Then("the customer clicks OK")
    public void clickOk() {
        orderPage.clickOkButton();
    }
}
