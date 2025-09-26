package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.HomePage;
import com.ecommerce.utils.JsonDataReader;
import com.ecommerce.utils.AssertLogger;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import com.ecommerce.pages.LoginPage;

import java.time.Duration;
import java.util.*;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@TestMethodOrder(OrderAnnotation.class)
@Epic("E-Commerce Application")
@Feature("Home Page")
public class HomePageTest extends BaseTest {

    private static Map<String, Map<String, String>> testData;
    private HomePage homePage;
    private LoginPage loginPage;


    @BeforeAll
    static void loadData() {
        testData = JsonDataReader.getTestData("homePageData.json");
    }

    @BeforeEach
    void setUpPages() {
        homePage = new HomePage();
        homePage.openBaseUrl();
        loginPage = new LoginPage();
    }

    // =========================
    // 🔹 Navbar Tests
    // =========================

    @Test
    @Order(1)
    @Story("Navbar")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify navbar links are visible when user is logged out")
    void testNavbarLinksLoggedOut() {
        String linksCsv = testData.get("navbar").get("expectedLinksLoggedOut");
        List<String> expectedLinks = Arrays.asList(linksCsv.split(","));

        for (String link : expectedLinks) {
            AssertLogger.assertTrueWithLog(
                    homePage.navLinks().findBy(com.codeborne.selenide.Condition.text(link)).exists(),
                    "Navbar should contain link: " + link
            );
        }
    }

    @Test
    @Order(2)
    @Story("Navbar")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login and signup links are visible when logged out")
    void testAuthLinksVisibleLoggedOut() {
        AssertLogger.assertTrueWithLog(
                homePage.isLoginVisible(),
                "Login link should be visible when logged out"
        );
        AssertLogger.assertTrueWithLog(
                homePage.isSignupVisible(),
                "Signup link should be visible when logged out"
        );
    }

    // =========================
    // 🔹 User State Tests
    // =========================

    @Test
    @Order(3)
    @Story("User State")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can log in and welcome message is displayed")
    void testUserLoggedInState() {
        String username = testData.get("user").get("username");
        String password = testData.get("user").get("password");

        // Perform login
        homePage.clickLogin();
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

        // Assert user is logged in
        AssertLogger.assertTrueWithLog(
                homePage.isUserLoggedIn(username),
                "User should be logged in and see welcome message: Welcome " + username
        );
    }

    @Test
    @Order(4)
    @Story("User State")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify logout state restores login and signup links")
    void testUserLoggedOutState() {
        String username = testData.get("user").get("username");
        String password = testData.get("user").get("password");

        // Ensure user is logged in first
        homePage.clickLogin();
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

        // Now perform logout
        homePage.clickLogout();

        // Assert user is logged out
        AssertLogger.assertTrueWithLog(
                homePage.isUserLoggedOut(),
                "User should be logged out and Login/Signup links should be visible"
        );
    }

    // =========================
    // 🔹 Slider Tests
    // =========================

    @Test
    @Order(5)
    @Story("Slider")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify slider is displayed on the homepage")
    void testSliderIsDisplayed() {
        AssertLogger.assertTrueWithLog(
                homePage.isSliderDisplayed(),
                "Slider should be visible on the homepage"
        );
    }

    @Test
    @Order(6)
    @Story("Slider")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify slider has at least the expected number of images")
    void testSliderImageCount() {
        int actualCount = homePage.getSliderImageCount();
        int expectedMin = Integer.parseInt(((Map<String, String>) testData.get("slider")).get("expectedImageCountMin"));

        AssertLogger.assertTrueWithLog(
                actualCount >= expectedMin,
                "Slider should have at least " + expectedMin + " images, but found " + actualCount
        );
    }

    // =========================
    // 🔹 Categories Tests
    // =========================

    @Test
    @Order(7)
    @Story("Categories")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify categories section is visible")
    void testCategoriesSectionVisible() {
        AssertLogger.assertTrueWithLog(
                homePage.isCategoriesVisible(),
                "Categories section should be visible on the homepage"
        );
    }


    @Test
    @Order(8)
    @Story("Categories")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Phones category filters products correctly")
    void testSelectPhonesCategory() {
        homePage.selectPhonesCategoryAndWait();

        AssertLogger.assertTrueWithLog(
                homePage.getProductCount() > 0,
                "Selecting 'Phones' should display at least one phone product"
        );
    }

    @Test
    @Order(9)
    @Story("Categories")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Laptops category filters products correctly")
    void testSelectLaptopsCategory() {
        homePage.selectLaptopsCategoryAndWait();

        AssertLogger.assertTrueWithLog(
                homePage.getProductCount() > 0,
                "Selecting 'Laptops' should display at least one laptop product"
        );
    }

    @Test
    @Order(10)
    @Story("Categories")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Monitors category filters products correctly")
    void testSelectMonitorsCategory() {
        homePage.selectMonitorsCategoryAndWait();

        AssertLogger.assertTrueWithLog(
                homePage.getProductCount() > 0,
                "Selecting 'Monitors' should display at least one monitor product"
        );
    }

    @Test
    @Order(11)
    @Story("Products")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify products are displayed on the homepage (count > 0)")
    void testProductsAreDisplayed() {
        homePage.waitForProductsToLoad();

        AssertLogger.assertTrueWithLog(
                homePage.getProductCount() > 0,
                "Homepage should display at least one product"
        );
    }

    @Test
    @Order(12)
    @Story("Products")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify each product card has a title and a price")
    void testEachProductHasTitleAndPrice() {
        homePage.waitForProductsToLoad();
        int productCount = homePage.getProductCount();

        for (int i = 0; i < productCount; i++) {
            String title = homePage.getProductTitle(i);
            String price = homePage.getProductPrice(i);

            AssertLogger.assertTrueWithLog(
                    title != null && !title.isBlank(),
                    "Product at index " + i + " should have a title"
            );

            AssertLogger.assertTrueWithLog(
                    price != null && !price.isBlank(),
                    "Product at index " + i + " should have a price"
            );
        }
    }

    @Test
    @Order(13)
    @Story("Products")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify clicking a product opens its Product Detail Page")
    void testClickProductOpensDetailPage() {
        homePage.waitForProductsToLoad();
        String firstProductTitle = homePage.getProductTitle(0);

        homePage.clickOnProduct(0);

        // Verify product detail page has the same title
        String detailPageTitle = $(".name").shouldBe(visible, Duration.ofSeconds(10)).getText();

        AssertLogger.assertTrueWithLog(
                detailPageTitle.equals(firstProductTitle),
                "Product detail page should display the selected product title. " +
                        "Expected: " + firstProductTitle + ", Actual: " + detailPageTitle
        );
    }

    @Test
    @Order(14)
    @Story("Products")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify the first page loads a maximum of 9 products")
    void testFirstPageLoadsMaxNineProducts() {
        homePage.waitForProductsToLoad();
        int productCount = homePage.getProductCount();

        AssertLogger.assertTrueWithLog(
                productCount <= 9,
                "First page should display at most 9 products, but found " + productCount
        );
    }

    @Test
    @Order(15)
    @Story("Products")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify product prices display in expected format ($<amount>)")
    void testProductPriceFormat() {
        homePage.waitForProductsToLoad();
        int productCount = homePage.getProductCount();

        for (int i = 0; i < productCount; i++) {
            String price = homePage.getProductPrice(i);

            boolean matchesFormat = price.matches("^\\$\\d+(\\.\\d{1,2})?$");
            AssertLogger.assertTrueWithLog(
                    matchesFormat,
                    "Product price should be in format $<amount>, but got: " + price
            );
        }
    }

    @Test
    @Order(16)
    @Story("Pagination")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Next button navigates to the next product page")
    void testNextButtonNavigatesToNextPage() {
        homePage.waitForProductsToLoad();

        // Capture product titles on page 1
        List<String> pageOneProducts = homePage.getAllProductTitles();
        String firstProductPageOne = pageOneProducts.get(0);

        // Navigate to page 2
        homePage.goToNextPageAndWait(firstProductPageOne);
        List<String> pageTwoProducts = homePage.getAllProductTitles();

        AssertLogger.assertTrueWithLog(
                !pageTwoProducts.equals(pageOneProducts),
                "Next button should navigate to a different product set"
        );
    }



    @Test
    @Order(17)
    @Story("Pagination")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Previous button navigates back to the previous product page")
    void testPreviousButtonNavigatesBackToPreviousPage() {
        homePage.waitForProductsToLoad();

        // Capture page 1 products
        List<String> pageOneProducts = homePage.getAllProductTitles();
        String firstProductPageOne = pageOneProducts.get(0);

        // Navigate to page 2
        homePage.goToNextPageAndWait(firstProductPageOne);
        List<String> pageTwoProducts = homePage.getAllProductTitles();

        AssertLogger.assertTrueWithLog(
                !pageTwoProducts.equals(pageOneProducts),
                "Next button should load a different product set"
        );

        // Navigate back to page 1
        String pageTwoFirst = pageTwoProducts.get(0);
        homePage.goToPreviousPageAndWait(pageTwoFirst);
        List<String> pageOneProductsAgain = homePage.getAllProductTitles();

        AssertLogger.assertTrueWithLog(
                pageOneProductsAgain.stream().anyMatch(pageOneProducts::contains),
                "Previous button should navigate back to page 1 (at least one original product should reappear)"
        );
    }



    @Test
    @Order(18)
    @Story("Pagination")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Next button is visible when multiple pages exist")
    void testNextButtonVisibleWhenMultiplePagesExist() {
        homePage.waitForProductsToLoad();

        AssertLogger.assertTrueWithLog(
                homePage.isNextButtonVisible(),
                "Next button should be visible when there are multiple product pages"
        );
    }

    @Test
    @Order(19)
    @Story("Pagination")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Previous button is hidden on the first page")
    void testPreviousButtonHiddenOnFirstPage() {
        homePage.waitForProductsToLoad();

        AssertLogger.assertTrueWithLog(
                !homePage.isPrevButtonVisible(),
                "Previous button should be hidden on the first page"
        );
    }

}

