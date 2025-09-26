package com.ecommerce.tests;

import com.codeborne.selenide.Selenide;
import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.CartPage;
import com.ecommerce.pages.HomePage;
import com.ecommerce.pages.OrderPage;
import com.ecommerce.pages.ProductPage;
import com.ecommerce.utils.AssertLogger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;

import static com.codeborne.selenide.Selenide.$;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("E-Commerce Order Tests")
@Feature("Order Functionality")
public class OrderPageTest extends BaseTest {

    private static JsonNode orderDataNode;
    private HomePage homePage;
    private ProductPage productPage;
    private CartPage cartPage;
    private OrderPage orderPage;

    @BeforeAll
    static void loadData() throws IOException {
        InputStream is = OrderPageTest.class.getClassLoader().getResourceAsStream("testdata/orderData.json");
        AssertLogger.assertNotNullWithLog(is, "orderData.json must exist in testdata folder");

        orderDataNode = new ObjectMapper().readTree(is);
    }

    @BeforeEach
    void setUpPages() {
        homePage = new HomePage();
        productPage = new ProductPage();
        cartPage = new CartPage();
        orderPage = new OrderPage();
        homePage.openBaseUrl();
    }

    @Test
    @Order(1)
    @Story("Order Modal Display")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the order modal is displayed when 'Place Order' button is clicked")
    void testOrderModalIsDisplayed() {
        // Add single product to cart
        JsonNode product = orderDataNode.get("singleOrder");
        String name = product.get("name").asText();
        String price = product.get("price").asText();

        homePage.clickOnProduct(name);
        productPage.clickAddToCart();
        productPage.clickCartNav(); // Go to cart

        // Verify product is in cart
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

        // Click Place Order
        cartPage.clickPlaceOrder();
        AssertLogger.assertTrueWithLog(
                orderPage.isOrderModalDisplayed(),
                "Order modal should be displayed when 'Place Order' is clicked"
        );
    }

    @Test
    @Order(2)
    @Story("Order Modal Title")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the modal title is correct")
    void testOrderModalTitle() {
        // Add single product to cart
        JsonNode product = orderDataNode.get("singleOrder");
        String name = product.get("name").asText();
        String price = product.get("price").asText();

        homePage.clickOnProduct(name);
        productPage.clickAddToCart();
        productPage.clickCartNav(); // Go to cart

        // Verify product is in cart
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

        // Click Place Order
        cartPage.clickPlaceOrder();

        String expectedTitle = "Place order"; // adjust if different
        AssertLogger.assertEqualsWithLog(
                expectedTitle,
                orderPage.getModalTitle(),
                "Modal title should match expected value"
        );
    }

    @Test
    @Order(3)
    @Story("Order Modal Total Amount")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the total amount displayed in the modal matches the cart total")
    void testOrderModalTotalAmount() {
        // Add single product to cart
        JsonNode product = orderDataNode.get("singleOrder");
        String name = product.get("name").asText();
        String price = product.get("price").asText();

        homePage.clickOnProduct(name);
        productPage.clickAddToCart();
        productPage.clickCartNav(); // Go to cart

        // Verify product is in cart
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

        // Capture cart total
        String cartTotal = cartPage.getTotalPrice();

        // Click Place Order
        cartPage.clickPlaceOrder();

        // Verify total in order modal
        String modalTotalRaw = orderPage.getTotalAmount();  // returns "Total: 360"
        String modalTotal = modalTotalRaw.replace("Total: ", "").trim();  // extract just the number

        AssertLogger.assertEqualsWithLog(
                cartTotal,
                modalTotal,
                "Total amount in order modal should match cart total"
        );
    }


    @Test
    @Order(4)
    @Story("Order Success Modal Display")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the success modal is displayed after clicking purchase")
    void testSuccessModalDisplayed() {
        JsonNode product = orderDataNode.get("singleOrder");
        String name = product.get("name").asText();
        String price = product.get("price").asText();

        homePage.clickOnProduct(name);
        productPage.clickAddToCart();
        productPage.clickCartNav(); // Go to cart

        // Verify product is in cart
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

        // Click Place Order
        cartPage.clickPlaceOrder();

        // Fill order details from JSON
        JsonNode orderDetails = orderDataNode.get("correctOrder");
        orderPage.enterName(orderDetails.get("name").asText());
        orderPage.enterCountry(orderDetails.get("country").asText());
        orderPage.enterCity(orderDetails.get("city").asText());
        orderPage.enterCard(orderDetails.get("card").asText());
        orderPage.enterMonth(orderDetails.get("month").asText());
        orderPage.enterYear(orderDetails.get("year").asText());

        // Click purchase
        orderPage.clickPurchase();

        // Assert success modal is displayed
        AssertLogger.assertTrueWithLog(
                orderPage.isSuccessModalDisplayed(),
                "Success modal should be displayed after purchase"
        );
    }

    @Test
    @Order(5)
    @Story("Order Submission Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that submitting an order with empty fields triggers an alert")
    void testEmptyOrderSubmission() {
        JsonNode product = orderDataNode.get("singleOrder");
        String name = product.get("name").asText();
        String price = product.get("price").asText();

        homePage.clickOnProduct(name);
        productPage.clickAddToCart();
        productPage.clickCartNav();

        // Verify product in cart
        AssertLogger.assertEqualsWithLog(name, cartPage.getProductName(0), "Product name in cart should match selected product");
        AssertLogger.assertEqualsWithLog(price, cartPage.getProductPrice(0), "Product price in cart should match selected product");

        cartPage.clickPlaceOrder();

        JsonNode orderDetails = orderDataNode.get("emptyFields");
        orderPage.enterName(orderDetails.get("name").asText());
        orderPage.enterCountry(orderDetails.get("country").asText());
        orderPage.enterCity(orderDetails.get("city").asText());
        orderPage.enterCard(orderDetails.get("card").asText());
        orderPage.enterMonth(orderDetails.get("month").asText());
        orderPage.enterYear(orderDetails.get("year").asText());

        // Click purchase
        orderPage.clickPurchase();

        // Validate alert
        String alertText = Selenide.switchTo().alert().getText();
        AssertLogger.assertEqualsWithLog("Please fill out Name and Creditcard.", alertText, "Alert should appear for empty fields");

        // Dismiss alert
        Selenide.switchTo().alert().accept();
    }


    @Test
    @Order(6)
    @Story("Order Submission Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that submitting an order with empty name triggers an alert")
    void testEmptyNameOrderSubmission() {
        JsonNode product = orderDataNode.get("singleOrder");
        String name = product.get("name").asText();
        String price = product.get("price").asText();

        homePage.clickOnProduct(name);
        productPage.clickAddToCart();
        productPage.clickCartNav();

        // Verify product in cart
        AssertLogger.assertEqualsWithLog(name, cartPage.getProductName(0), "Product name in cart should match selected product");
        AssertLogger.assertEqualsWithLog(price, cartPage.getProductPrice(0), "Product price in cart should match selected product");

        cartPage.clickPlaceOrder();

        JsonNode orderDetails = orderDataNode.get("emptyName");
        orderPage.enterName(orderDetails.get("name").asText());
        orderPage.enterCountry(orderDetails.get("country").asText());
        orderPage.enterCity(orderDetails.get("city").asText());
        orderPage.enterCard(orderDetails.get("card").asText());
        orderPage.enterMonth(orderDetails.get("month").asText());
        orderPage.enterYear(orderDetails.get("year").asText());

        // Click purchase
        orderPage.clickPurchase();

        // Validate alert
        String alertText = Selenide.switchTo().alert().getText();
        AssertLogger.assertEqualsWithLog("Please fill out Name and Creditcard.", alertText, "Alert should appear for empty fields");

        // Dismiss alert
        Selenide.switchTo().alert().accept();
    }



}
