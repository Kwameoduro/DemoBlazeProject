package com.ecommerce.utils;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    /**
     * Initialize Selenide WebDriver configuration
     */
    public static void setupDriver() {
        // Apply configuration from config.json
        Configuration.baseUrl = ConfigReader.getBaseUrl();
        Configuration.browser = ConfigReader.getBrowser();
        Configuration.timeout = ConfigReader.getTimeout();
        Configuration.headless = ConfigReader.isHeadless();
        Configuration.screenshots = ConfigReader.isScreenshotOnFailure();
        Configuration.savePageSource = ConfigReader.isSavePageSourceOnFailure();
        Configuration.reportsFolder = ConfigReader.getAllureResultsDir();

        // Do NOT set browserSize here — let WebDriver handle it below
        Configuration.browserSize = null;

        WebDriver driver;

        switch (Configuration.browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (ConfigReader.isHeadless()) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (ConfigReader.isHeadless()) {
                    firefoxOptions.addArguments("--headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (ConfigReader.isHeadless()) {
                    edgeOptions.addArguments("--headless=new");
                }
                driver = new EdgeDriver(edgeOptions);
                break;

            default:
                throw new RuntimeException("Unsupported browser: " + Configuration.browser);
        }

        // ✅ Force fullscreen or maximize after driver creation
        if (ConfigReader.isFullscreen()) {
            driver.manage().window().fullscreen();
        } else if (ConfigReader.isMaximize()) {
            driver.manage().window().maximize();
        }

        WebDriverRunner.setWebDriver(driver);
    }

    /**
     * Get the current WebDriver instance
     */
    public static WebDriver getDriver() {
        return WebDriverRunner.getWebDriver();
    }

    /**
     * Quit the WebDriver and clean up resources
     */
    public static void quitDriver() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            WebDriverRunner.closeWebDriver();
        }
    }
}
