package com.ecommerce.steps;

import com.ecommerce.pages.ContactPage;
import com.ecommerce.pages.HomePage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class ContactSteps {

    private final ContactPage contactPage = new ContactPage();
    private final HomePage homePage = new HomePage();

    @Given("the customer is on the homepage")
    public void theCustomerIsOnTheHomepage() {
        open("/");
    }

    @Given("the customer opens the Contact form")
    public void openContactForm() {
        homePage.clickContact();
        assertTrue(contactPage.isModalDisplayed(),
                "Contact modal should be visible after clicking Contact");
    }

    @When("the customer fills the Contact form with:")
    public void fillContactForm(DataTable table) {
        // DataTable → Map<String, String>
        Map<String, String> data = table.asMap(String.class, String.class);

        contactPage.enterEmail(data.get("email"));
        contactPage.enterName(data.get("name"));
        contactPage.enterMessage(data.get("message"));
    }

    @When("the customer clicks Send message")
    public void clickSendMessage() {
        contactPage.clickSendMessage();
    }

    @Then("an alert should appear with text {string}")
    public void verifyAlertText(String expected) {
        String actual = contactPage.getAlertText();
        assertEquals(expected, actual, "Alert text should match expected message");
    }

    @Then("the customer accepts the alert")
    public void acceptAlert() {
        contactPage.acceptAlert();
    }

    @When("the customer clicks the Contact form close icon")
    public void clickCloseIcon() {
        contactPage.clickCloseIcon();
    }

    @Then("the Contact form should not be visible")
    public void verifyModalNotVisible() {
        assertFalse(contactPage.isModalDisplayed(),
                "Contact modal should not be visible after closing");
    }
}
