package com.ecommerce.hooks;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import static com.codeborne.selenide.Selenide.*;

public class TestHooks {

    @Before
    public void setUp() {
        Configuration.baseUrl = "https://www.demoblaze.com";
        Configuration.headless = Boolean.parseBoolean(System.getProperty("selenide.headless", "false"));
        Configuration.timeout = 10000; // 10s
        Configuration.savePageSource = false;
        Configuration.screenshots = true;

        open("/"); // ensures WebDriver is bound
        WebDriverRunner.getWebDriver().manage().window().maximize();
    }

    @After
    public void tearDown() {
        closeWebDriver();
    }
}
