package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.CartPage;
import com.ecommerce.pages.HomePage;
import com.ecommerce.pages.ProductPage;
import com.ecommerce.utils.AssertLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("E-Commerce Cart Tests")
@Feature("Cart Functionality")
public class CartPageTest extends BaseTest {

    private static JsonNode cartDataNode;
    private HomePage homePage;
    private ProductPage productPage;
    private CartPage cartPage;

    @BeforeAll
    static void loadData() throws IOException {
        InputStream is = CartPageTest.class.getClassLoader().getResourceAsStream("testdata/cartData.json");
        AssertLogger.assertNotNullWithLog(is, "cartData.json must exist in testdata folder");

        cartDataNode = new ObjectMapper().readTree(is);
    }

    @BeforeEach
    void setUpPages() {
        homePage = new HomePage();
        productPage = new ProductPage();
        cartPage = new CartPage();
        homePage.openBaseUrl();  // Open the e-commerce home page
    }

    @Test
    @Order(1)
    @Story("Cart Page")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that the cart page loads successfully")
    void testCartPageLoadsSuccessfully() {
        homePage.clickCart();
        AssertLogger.assertTrueWithLog(
                cartPage.isCartTableVisible(),
                "Cart page should load with product table visible"
        );
    }

    @Test
    @Order(2)
    @Story("Cart Page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that 'Place Order' button is visible on cart page")
    void testPlaceOrderButtonIsVisible() {
        homePage.clickCart();
        AssertLogger.assertTrueWithLog(
                cartPage.isPlaceOrderButtonVisible(),
                "'Place Order' button should be visible"
        );
    }

    @Test
    @Order(3)
    @Story("Cart Add Single Product")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a single product can be added to the cart")
    void testAddSingleProductToCart() {
        JsonNode product = cartDataNode.get("singleProduct");
        String name = product.get("name").asText();
        String price = product.get("price").asText();

        homePage.clickOnProduct(name);   // FIXED: pass name directly
        productPage.clickAddToCart();
        productPage.clickCartNav();       // Go to cart

        AssertLogger.assertEqualsWithLog(
                name,
                cartPage.getProductName(0),
                "Product name in cart should match selected product"
        );
        AssertLogger.assertEqualsWithLog(
                price,
                cartPage.getProductPrice(0),
                "Product price in cart should match selected product"
        );
    }

    @Test
    @Order(4)
    @Story("Cart Delete Product")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a product can be deleted from the cart")
    void testDeleteProductFromCart() {
        JsonNode product = cartDataNode.get("singleProduct");
        String name = product.get("name").asText();

        homePage.clickOnProduct(name);
        productPage.clickAddToCart();
        productPage.clickCartNav(); // Go to cart

        cartPage.deleteProduct(0);

        AssertLogger.assertTrueWithLog(
                cartPage.isCartEmpty(),
                "Cart should be empty after deleting the product"
        );
    }

}
