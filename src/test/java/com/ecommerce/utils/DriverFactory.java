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
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {

    public static void setupDriver() {
        Configuration.baseUrl = ConfigReader.getBaseUrl();
        Configuration.browser = ConfigReader.getBrowser();
        Configuration.timeout = ConfigReader.getTimeout();
        Configuration.headless = ConfigReader.isHeadless();
        Configuration.screenshots = ConfigReader.isScreenshotOnFailure();
        Configuration.savePageSource = ConfigReader.isSavePageSourceOnFailure();
        Configuration.reportsFolder = ConfigReader.getAllureResultsDir();

        Configuration.browserSize = null;

        WebDriver driver;

        // ✅ Check if we are running in CI / Docker
        String remoteUrl = System.getenv("SELENIDE_REMOTE_URL");
        boolean isCI = "true".equalsIgnoreCase(System.getenv("CI"));

        if (isCI && remoteUrl != null) {
            // Run with RemoteWebDriver (Docker/Grid)
            try {
                switch (Configuration.browser.toLowerCase()) {
                    case "chrome":
                        ChromeOptions chromeOptions = new ChromeOptions();
                        if (ConfigReader.isHeadless()) {
                            chromeOptions.addArguments("--headless=new");
                        }
                        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                        driver = new RemoteWebDriver(new URL(remoteUrl), chromeOptions);
                        break;

                    case "firefox":
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        if (ConfigReader.isHeadless()) {
                            firefoxOptions.addArguments("--headless");
                        }
                        driver = new RemoteWebDriver(new URL(remoteUrl), firefoxOptions);
                        break;

                    case "edge":
                        EdgeOptions edgeOptions = new EdgeOptions();
                        if (ConfigReader.isHeadless()) {
                            edgeOptions.addArguments("--headless=new");
                        }
                        driver = new RemoteWebDriver(new URL(remoteUrl), edgeOptions);
                        break;

                    default:
                        throw new RuntimeException("Unsupported browser: " + Configuration.browser);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid Remote WebDriver URL: " + remoteUrl, e);
            }
        } else {
            // Run locally
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
        }

        // ✅ Maximize / fullscreen
        if (ConfigReader.isFullscreen()) {
            driver.manage().window().fullscreen();
        } else if (ConfigReader.isMaximize()) {
            driver.manage().window().maximize();
        }

        WebDriverRunner.setWebDriver(driver);
    }

    public static WebDriver getDriver() {
        return WebDriverRunner.getWebDriver();
    }

    public static void quitDriver() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            WebDriverRunner.closeWebDriver();
        }
    }
}
