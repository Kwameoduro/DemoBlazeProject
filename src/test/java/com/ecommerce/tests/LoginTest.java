package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.HomePage;
import com.ecommerce.pages.LoginPage;
import com.ecommerce.utils.AssertLogger;
import com.ecommerce.utils.JsonDataReader;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("E-Commerce Application")
@Feature("Login")
@Tag("smoke")
public class LoginTest extends BaseTest {

    private static Map<String, Map<String, String>> testData;
    private HomePage homePage;
    private LoginPage loginPage;

    @BeforeAll
    static void loadData() {
        testData = JsonDataReader.getTestData("loginData.json");
    }

    @BeforeEach
    void setUpPages() {
        homePage = new HomePage();
        loginPage = new LoginPage();
        homePage.openBaseUrl(); // ✅ ensures DemoBlaze is opened before each test
    }

    @Test
    @Order(1)
    @Story("User Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify successful login with valid credentials")
    void testValidLogin() {
        Map<String, String> data = testData.get("validUser");
        homePage.clickLogin();
        loginPage.enterUsername(data.get("username"));
        loginPage.enterPassword(data.get("password"));
        loginPage.clickLoginButton();

        AssertLogger.assertTrueWithLog(
                homePage.isUserLoggedIn(data.get("username")),
                "User should be logged in successfully"
        );
    }

    @Test
    @Order(2)
    @Story("User Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify login fails with invalid password")
    void testInvalidPassword() {
        Map<String, String> data = testData.get("invalidPassword");
        homePage.clickLogin();
        loginPage.enterUsername(data.get("username"));
        loginPage.enterPassword(data.get("password"));
        loginPage.clickLoginButton();

        String alertText = loginPage.getAlertText();
        AssertLogger.assertEqualsWithLog(
                "Wrong password.",
                alertText,
                "Alert should indicate wrong password"
        );
        loginPage.acceptAlert();
    }

    @Test
    @Order(3)
    @Story("User Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify login fails with invalid username")
    void testInvalidUsername() {
        Map<String, String> data = testData.get("invalidUsername");
        homePage.clickLogin();
        loginPage.enterUsername(data.get("username"));
        loginPage.enterPassword(data.get("password"));
        loginPage.clickLoginButton();

        String alertText = loginPage.getAlertText();
        AssertLogger.assertEqualsWithLog(
                "User does not exist.",
                alertText,
                "Alert should indicate invalid username"
        );
        loginPage.acceptAlert();
    }

    @Test
    @Order(4)
    @Story("User Login")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify login fails with empty username")
    void testEmptyUsername() {
        Map<String, String> data = testData.get("emptyUsername");
        homePage.clickLogin();
        loginPage.enterUsername(data.get("username"));
        loginPage.enterPassword(data.get("password"));
        loginPage.clickLoginButton();

        String alertText = loginPage.getAlertText();
        AssertLogger.assertEqualsWithLog(
                "Please fill out Username and Password.",
                alertText,
                "Alert should indicate missing username"
        );
        loginPage.acceptAlert();
    }

    @Test
    @Order(5)
    @Story("User Login")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify login fails with empty password")
    void testEmptyPassword() {
        Map<String, String> data = testData.get("emptyPassword");
        homePage.clickLogin();
        loginPage.enterUsername(data.get("username"));
        loginPage.enterPassword(data.get("password"));
        loginPage.clickLoginButton();

        String alertText = loginPage.getAlertText();
        AssertLogger.assertEqualsWithLog(
                "Please fill out Username and Password.",
                alertText,
                "Alert should indicate missing password"
        );
        loginPage.acceptAlert();
    }

    @Test
    @Order(6)
    @Story("User Login")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify login fails with both username and password empty")
    void testEmptyCredentials() {
        Map<String, String> data = testData.get("emptyCredentials");
        homePage.clickLogin();
        loginPage.enterUsername(data.get("username"));
        loginPage.enterPassword(data.get("password"));
        loginPage.clickLoginButton();

        String alertText = loginPage.getAlertText();
        AssertLogger.assertEqualsWithLog(
                "Please fill out Username and Password.",
                alertText,
                "Alert should indicate empty credentials"
        );
        loginPage.acceptAlert();
    }

    // this test passes because the user doesn't exist, but then, if it exists, it will log in successfully.
    @Test
    @Order(7)
    @Story("User Login Security")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login fails with SQL Injection attempt")
    void testSQLInjectionLogin() {
        Map<String, String> data = testData.get("sqlInjection");
        homePage.clickLogin();
        loginPage.enterUsername(data.get("username"));
        loginPage.enterPassword(data.get("password"));
        loginPage.clickLoginButton();

        String alertText = loginPage.getAlertText();
        AssertLogger.assertEqualsWithLog(
                "User does not exist.",
                alertText,
                "Alert should indicate SQL Injection attempt failed"
        );
        loginPage.acceptAlert();
    }


    // this test passes because the user doesn't exist, but then, if it exists, it will log in successfully.
    @Test
    @Order(8)
    @Story("User Login Security")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login fails with XSS attempt")
    void testXSSLogin() {
        Map<String, String> data = testData.get("xssAttempt");
        homePage.clickLogin();
        loginPage.enterUsername(data.get("username"));
        loginPage.enterPassword(data.get("password"));
        loginPage.clickLoginButton();

        String alertText = loginPage.getAlertText();
        AssertLogger.assertEqualsWithLog(
                "User does not exist.",
                alertText,
                "Alert should indicate XSS attempt failed"
        );
        loginPage.acceptAlert();
    }

}
