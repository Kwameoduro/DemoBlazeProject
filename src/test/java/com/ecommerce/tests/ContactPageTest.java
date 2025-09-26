package com.ecommerce.tests;

import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.ContactPage;
import com.ecommerce.pages.HomePage;
import com.ecommerce.utils.AssertLogger;
import com.ecommerce.utils.JsonDataReader;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.Map;

import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@TestMethodOrder(OrderAnnotation.class)
@Epic("E-Commerce Application")
@Feature("Contact Form")
public class ContactPageTest extends BaseTest {

    private static Map<String, Map<String, String>> testData;
    private HomePage homePage;
    private ContactPage contactPage;

    @BeforeAll
    static void loadData() {
        // Load test data once before all tests
        testData = JsonDataReader.getTestData("contactData.json");
    }

    @BeforeEach
    void setUpPages() {
        homePage = new HomePage();
        contactPage = new ContactPage();
        homePage.openBaseUrl();
    }

    @Test
    @Order(1)
    @Story("Contact Modal")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the Contact modal opens and displays correct title")
    void testContactModalOpens() {
        homePage.clickContact();
        AssertLogger.assertTrueWithLog(
                contactPage.isModalDisplayed(),
                "Contact modal should be displayed"
        );

        AssertLogger.assertEqualsWithLog(
                "New message",
                contactPage.getModalTitle(),
                "Modal title should be 'New message'"
        );
    }

    @Test
    @Order(2)
    @Story("Contact Modal")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Submit contact form with valid data")
    void testSubmitValidMessage() {
        Map<String, String> data = testData.get("validMessage");

        homePage.clickContact();
        contactPage.enterEmail(data.get("email"));
        contactPage.enterName(data.get("name"));
        contactPage.enterMessage(data.get("message"));
        contactPage.clickSendMessage();

        String alertText = contactPage.getAlertText();
        contactPage.acceptAlert();

        AssertLogger.assertEqualsWithLog(
                data.get("expectedResult"),
                alertText,
                "Alert text should confirm message sent"
        );
    }

    @Test
    @Order(3)
    @Story("Contact Modal")
    @Severity(SeverityLevel.NORMAL)
    @Description("Attempt to submit message with missing email")
    void testSubmitWithoutEmail() {
        Map<String, String> data = testData.get("missingEmail");

        homePage.clickContact();
        contactPage.enterName(data.get("name"));
        contactPage.enterMessage(data.get("message"));
        contactPage.clickSendMessage();

        String alertText = contactPage.getAlertText();
        contactPage.acceptAlert();

        AssertLogger.assertEqualsWithLog(
                data.get("expectedResult"),
                alertText,
                "Alert text should indicate missing email (Bug if it still accepts)"
        );
    }

    @Test
    @Order(4)
    @Story("Contact Modal")
    @Severity(SeverityLevel.NORMAL)
    @Description("Attempt to submit message with missing name")
    void testSubmitWithoutName() {
        Map<String, String> data = testData.get("missingName");

        homePage.clickContact();
        contactPage.enterEmail(data.get("email"));
        contactPage.enterMessage(data.get("message"));
        contactPage.clickSendMessage();

        String alertText = contactPage.getAlertText();
        contactPage.acceptAlert();

        AssertLogger.assertEqualsWithLog(
                data.get("expectedResult"),
                alertText,
                "Alert text should indicate missing name (Bug if it still accepts)"
        );
    }

    @Test
    @Order(5)
    @Story("Contact Modal")
    @Severity(SeverityLevel.NORMAL)
    @Description("Attempt to submit message with missing message body")
    void testSubmitWithoutMessage() {
        Map<String, String> data = testData.get("missingMessage");

        homePage.clickContact();
        contactPage.enterEmail(data.get("email"));
        contactPage.enterName(data.get("name"));
        contactPage.clickSendMessage();

        String alertText = contactPage.getAlertText();
        contactPage.acceptAlert();

        AssertLogger.assertEqualsWithLog(
                data.get("expectedResult"),
                alertText,
                "Alert text should indicate missing message body (Bug if it still accepts)"
        );
    }

    @Test
    @Order(6)
    @Story("Contact Modal")
    @Severity(SeverityLevel.NORMAL)
    @Description("Attempt to submit form with all fields empty")
    void testSubmitWithAllEmptyFields() {
        Map<String, String> data = testData.get("emptyFields");

        homePage.clickContact();
        contactPage.clickSendMessage();

        String alertText = contactPage.getAlertText();
        contactPage.acceptAlert();

        AssertLogger.assertEqualsWithLog(
                data.get("expectedResult"),
                alertText,
                "Alert text should indicate invalid submission (Bug if it still accepts)"
        );
    }

    @Test
    @Order(7)
    @Story("Contact Modal")
    @Severity(SeverityLevel.NORMAL)
    @Description("Attempt to submit message with invalid email")
    void testSubmitWithInvalidEmail() {
        Map<String, String> data = testData.get("invalidEmail");

        homePage.clickContact();
        contactPage.enterEmail(data.get("email"));
        contactPage.enterName(data.get("name"));
        contactPage.enterMessage(data.get("message"));
        contactPage.clickSendMessage();

        String alertText = contactPage.getAlertText();
        contactPage.acceptAlert();

        AssertLogger.assertEqualsWithLog(
                data.get("expectedResult"),
                alertText,
                "Alert text should indicate invalid email (Bug if it still accepts)"
        );
    }


    @Test
    @Order(8)
    @Story("Contact Modal")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify the Contact modal can be closed with the Close button")
    void testCloseModalWithCloseButton() {
        homePage.clickContact();
        contactPage.clickCloseButton();

        AssertLogger.assertTrueWithLog(
                contactPage.isModalDisplayed(),
                "Contact modal should be closed after clicking Close button"
        );
    }

    @Test
    @Order(9)
    @Story("Contact Modal")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify the Contact modal can be closed with the top-right X icon")
    void testCloseModalWithCloseIcon() {
        homePage.clickContact();
        contactPage.clickCloseIcon();

        AssertLogger.assertTrueWithLog(
                contactPage.isModalDisplayed(),
                "Contact modal should be closed after clicking X icon"
        );
    }
}
