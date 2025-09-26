package com.ecommerce.tests;



import com.ecommerce.base.BaseTest;
import com.ecommerce.pages.AboutUsPage;
import com.ecommerce.pages.HomePage;
import com.ecommerce.utils.AssertLogger;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("E-Commerce Application")
@Feature("About Us Modal")
public class AboutUsPageTest extends BaseTest {

    private HomePage homePage;
    private AboutUsPage aboutUsPage;

    @BeforeEach
    void setUpPages() {
        homePage = new HomePage();
        aboutUsPage = new AboutUsPage();
        homePage.openBaseUrl();
    }

    @Test
    @Order(1)
    @Story("About Us Modal")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify About Us modal opens and is displayed with correct title")
    void testOpenAboutUsModal() {
        homePage.clickAboutUs();
        AssertLogger.assertTrueWithLog(
                aboutUsPage.isModalDisplayed(),
                "About Us modal should be displayed"
        );
        AssertLogger.assertEqualsWithLog(
                "About us",
                aboutUsPage.getModalTitle(),
                "Modal title should be 'About us'"
        );
    }

    @Test
    @Order(2)
    @Story("About Us Modal")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify About Us modal contains video/media content")
    void testModalContainsMedia() {
        homePage.clickAboutUs();
        AssertLogger.assertTrueWithLog(
                aboutUsPage.isMediaDisplayed(),
                "About Us modal should display media content"
        );
    }

    @Test
    @Order(3)
    @Story("About Us Modal")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Close button is visible and can close the modal")
    void testCloseModalWithCloseButton() {
        homePage.clickAboutUs();
        aboutUsPage.clickCloseButton();

        AssertLogger.assertFalseWithLog(
                aboutUsPage.isModalDisplayed(),
                "About Us modal should be closed after clicking Close button"
        );
    }


    @Test
    @Order(4)
    @Story("About Us Modal")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify Close icon (X) closes the modal")
    void testCloseModalWithCloseIcon() {
        homePage.clickAboutUs();
        aboutUsPage.clickCloseIcon();

        AssertLogger.assertFalseWithLog(
                aboutUsPage.isModalDisplayed(),
                "About Us modal should be closed after clicking Close icon"
        );
    }
}
