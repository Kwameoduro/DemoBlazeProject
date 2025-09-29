package com.ecommerce.tests;




import com.ecommerce.base.BaseTest;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import com.ecommerce.pages.HomePage;
import com.ecommerce.pages.SignupPage;
import com.ecommerce.utils.JsonDataReader;
import com.ecommerce.utils.AssertLogger;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("regression")
public class SignupTest extends BaseTest {

    private HomePage homePage;
    private SignupPage signupPage;
    private static Map<String, Map<String, String>> testData;

    @BeforeAll
    static void loadData() {
        testData = JsonDataReader.getTestData("signupData.json");
    }

    @BeforeEach
    void setupPages() {
        homePage = new HomePage();
        signupPage = new SignupPage();
        homePage.openBaseUrl();
    }

    @Test
    @Story("User Registration")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify successful signup with valid credentials")
    void testValidSignup() {
        Map<String, String> data = testData.get("validSignup");
        homePage.clickSignup();
        assertTrue(signupPage.isModalDisplayed(), "Signup modal should be visible");

        signupPage.enterUsername(data.get("username"));
        signupPage.enterPassword(data.get("password"));
        signupPage.clickSignupButton();

        String alertText = signupPage.getAlertText();
        AssertLogger.assertEqualsWithLog("Sign up successful.", alertText, "Alert text should confirm signup success");
        signupPage.acceptAlert();
    }

    @Test
    @Story("User Registration")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify signup fails with short password")
    void testShortPassword() {
        Map<String, String> data = testData.get("shortPassword");
        homePage.clickSignup();
        signupPage.enterUsername(data.get("username"));
        signupPage.enterPassword(data.get("password"));
        signupPage.clickSignupButton();

        String alertText = signupPage.getAlertText();
        AssertLogger.assertTrueWithLog(alertText.contains("password"), "Alert should indicate password issue");
        signupPage.acceptAlert();
    }

    @Test
    @Story("User Registration")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify signup fails with empty username")
    void testEmptyUsername() {
        Map<String, String> data = testData.get("emptyUsername");
        homePage.clickSignup();
        signupPage.enterUsername(data.get("username"));
        signupPage.enterPassword(data.get("password"));
        signupPage.clickSignupButton();

        String alertText = signupPage.getAlertText();
        AssertLogger.assertTrueWithLog(alertText.contains("username"), "Alert should mention username issue");
        signupPage.acceptAlert();
    }

    @Test
    @Story("User Registration")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify signup fails with empty password")
    void testEmptyPassword() {
        Map<String, String> data = testData.get("emptyPassword");
        homePage.clickSignup();
        signupPage.enterUsername(data.get("username"));
        signupPage.enterPassword(data.get("password"));
        signupPage.clickSignupButton();

        String alertText = signupPage.getAlertText();
        AssertLogger.assertTrueWithLog(alertText.contains("password"), "Alert should mention password issue");
        signupPage.acceptAlert();
    }

    @Test
    @Story("User Registration")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify signup fails with existing username")
    void testExistingUserSignup() {
        Map<String, String> data = testData.get("existingUser");
        homePage.clickSignup();
        signupPage.enterUsername(data.get("username"));
        signupPage.enterPassword(data.get("password"));
        signupPage.clickSignupButton();

        String alertText = signupPage.getAlertText();
        AssertLogger.assertTrueWithLog(alertText.contains("This user already exist"), "Alert should indicate user exists");
        signupPage.acceptAlert();
    }

    @Test
    @Story("User Registration")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify signup with username containing special characters")
    void testSpecialCharsUsername() {
        Map<String, String> data = testData.get("specialCharsUsername");
        homePage.clickSignup();
        signupPage.enterUsername(data.get("username"));
        signupPage.enterPassword(data.get("password"));
        signupPage.clickSignupButton();

        String alertText = signupPage.getAlertText();
        AssertLogger.assertTrueWithLog(alertText.contains("Username cannot be special characters"), "Alert should appear for invalid username");
        signupPage.acceptAlert();
    }

    @Test
    @Story("User Registration")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify signup fails with very long username")
    void testLongUsername() {
        Map<String, String> data = testData.get("longUsername");
        homePage.clickSignup();
        signupPage.enterUsername(data.get("username"));
        signupPage.enterPassword(data.get("password"));
        signupPage.clickSignupButton();

        String alertText = signupPage.getAlertText();
        AssertLogger.assertTrueWithLog(alertText.contains("username"), "Alert should mention username issue");
        signupPage.acceptAlert();
    }

    @Test
    @Story("User Registration")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify signup fails with SQL injection attempt in username")
    void testSqlInjectionAttempt() {
        Map<String, String> data = testData.get("sqlInjectionAttempt");
        homePage.clickSignup();
        signupPage.enterUsername(data.get("username"));
        signupPage.enterPassword(data.get("password"));
        signupPage.clickSignupButton();

        String alertText = signupPage.getAlertText();
        AssertLogger.assertTrueWithLog(Boolean.parseBoolean(alertText), "System should display an error for SQL injection attempt");
        signupPage.acceptAlert();
    }


    @Test
    @Story("User Registration")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify signup fails with too short username and password")
    void testShortUsernameAndPassword() {
        Map<String, String> data = testData.get("shortUsernamePassword");
        homePage.clickSignup();
        signupPage.enterUsername(data.get("username"));
        signupPage.enterPassword(data.get("password"));
        signupPage.clickSignupButton();

        String alertText = signupPage.getAlertText();
        AssertLogger.assertNotEqualsWithLog("Sign up successful.", alertText,
                "Signup should not succeed with single-character username and password");
        AssertLogger.assertTrueWithLog(alertText.toLowerCase().contains("username") || alertText.toLowerCase().contains("password"),
                "Alert should mention an issue with username or password length");
        signupPage.acceptAlert();
    }
}

