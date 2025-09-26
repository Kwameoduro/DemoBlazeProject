package com.ecommerce.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.DriverFactory;
import io.qameta.allure.Attachment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;


import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Selenide.screenshot;

public abstract class BaseTest {

    @BeforeEach
    void setup(TestInfo testInfo) {
        System.out.println("=== Starting Test: " + testInfo.getDisplayName() + " ===");

        // ✅ Ensure consistent viewport across local and CI
        Configuration.browserSize = "1920x1080";       //
        // Or, if you want fixed resolution instead of full maximize, use:
        // Configuration.browserSize = "1920x1080";

        // ✅ Base URL from config.json
        Configuration.baseUrl = ConfigReader.getBaseUrl();

        DriverFactory.setupDriver();
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        if (WebDriverRunner.hasWebDriverStarted()) {
            boolean failed = testInfo.getTags().contains("failed"); // tags can mark failures
            if (failed) {
                attachScreenshot();
                attachPageSource();
            }
            DriverFactory.quitDriver();
        }
        System.out.println("=== Finished Test: " + testInfo.getDisplayName() + " ===");
    }

    /**
     * Attach a screenshot to Allure report
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] attachScreenshot() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
        }
        return new byte[0];
    }

    /**
     * Attach page source to Allure report
     */
    @Attachment(value = "Page Source", type = "text/html")
    public byte[] attachPageSource() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            return WebDriverRunner.source().getBytes(StandardCharsets.UTF_8);
        }
        return new byte[0];
    }

    /**
     * Helper: manually trigger screenshot in tests
     */
    protected void captureScreenshot(String name) {
        screenshot(name);
    }

    /**
     * Access base URL (from config.json)
     */
    protected String getBaseUrl() {
        return ConfigReader.getBaseUrl();
    }
}
