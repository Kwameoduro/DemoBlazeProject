package com.ecommerce.steps;

import com.ecommerce.pages.HomePage;
import com.ecommerce.pages.ProductPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeSteps {

    private final HomePage homePage = new HomePage();
    private final ProductPage productPage = new ProductPage();

    // ------------------- Homepage Steps -------------------

    @Given("the user is on the homepage")
    public void theUserIsOnTheHomepage() {
        assertTrue(homePage.isHomePageDisplayed(), "Homepage should be displayed");
    }

    @Then("the homepage should display a list of products")
    public void homepageShouldDisplayListOfProducts() {
        List<String> productTitles = homePage.getAllProductTitles();
        assertTrue(!productTitles.isEmpty(), "Homepage should display at least one product");
    }

    @Then("each product should show a name, price and thumbnail")
    public void eachProductShouldShowDetails() {
        List<String> productTitles = homePage.getAllProductTitles();
        for (int i = 0; i < productTitles.size(); i++) {
            String name = homePage.getProductTitle(i);
            String price = homePage.getProductPrice(i);
            String thumbnail = homePage.getProductThumbnail(i);

            assertTrue(!name.isEmpty(), "Product name should not be empty");
            assertTrue(!price.isEmpty(), "Product price should not be empty");
            assertTrue(!thumbnail.isEmpty(), "Product thumbnail should not be empty");
        }
    }

    // ------------------- Product Navigation Steps -------------------

    @When("the user clicks the product {string}")
    public void userClicksProduct(String productName) {
        homePage.clickOnProduct(productName);
    }

    @Then("the product detail page should show the product name {string}")
    public void productDetailPageShouldShowProductName(String expectedName) {
        assertEquals(expectedName, productPage.getProductTitle(),
                "Product detail page should show correct product name");
    }

    @Then("the product price should be visible")
    public void productPriceShouldBeVisible() {
        assertTrue(!productPage.getProductPrice().isEmpty(), "Product price should be visible");
    }

    @Then("the {string} button should be displayed")
    public void buttonShouldBeDisplayed(String buttonText) {
        assertTrue(productPage.isAddToCartButtonVisible(),
                buttonText + " button should be visible on product page");
    }

    // ------------------- Navigation Menu Steps -------------------

    @Then("the navigation menu should display the following options:")
    public void navigationMenuShouldDisplayOptions(DataTable table) {
        List<String> expectedLinks = table.asList();
        List<String> actualLinks = homePage.navLinks().texts();
        for (String link : expectedLinks) {
            assertTrue(actualLinks.contains(link.trim()), "Navigation link should be visible: " + link);
        }
    }

    // ------------------- Slider Steps -------------------

    @Then("the homepage slider should display {int} images")
    public void homepageSliderShouldDisplayImages(int expectedCount) {
        assertTrue(homePage.isSliderDisplayed(), "Slider should be visible");
        assertEquals(expectedCount, homePage.getSliderImageCount(),
                "Slider image count should match expected");
    }

    // ------------------- Categories Steps -------------------

    @Then("the categories section should display:")
    public void categoriesSectionShouldDisplay(DataTable table) {
        List<String> expectedCategories = table.asList();
        assertTrue(homePage.isCategoriesVisible(), "Categories section should be visible");
        for (String category : expectedCategories) {
            assertTrue(homePage.getCategories().contains(category),
                    "Category should be visible: " + category);
        }
    }
}
