package com.ecommerce.utils;




import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.webdriver;

/**
 * Utility class for explicit and custom waits in Selenide tests.
 */
public class WaitUtils {

    private static final int DEFAULT_TIMEOUT = 5000; // 5 seconds

    /**
     * Waits for an element to be visible within the default timeout.
     *
     * @param element SelenideElement to wait for
     */
    public static void waitForVisibility(SelenideElement element) {
        element.shouldBe(com.codeborne.selenide.Condition.visible, Duration.ofMillis(DEFAULT_TIMEOUT));
    }

    /**
     * Waits for an element to disappear (be hidden or removed from DOM).
     *
     * @param element SelenideElement to wait for
     */
    public static void waitForInvisibility(SelenideElement element) {
        element.shouldBe(com.codeborne.selenide.Condition.disappear, Duration.ofMillis(DEFAULT_TIMEOUT));
    }

    /**
     * Waits for an element to contain specific text.
     *
     * @param element SelenideElement to check
     * @param text    expected text
     */
    public static void waitForText(SelenideElement element, String text) {
        element.shouldHave(com.codeborne.selenide.Condition.text(text), Duration.ofMillis(DEFAULT_TIMEOUT));
    }

    /**
     * Waits until a custom ExpectedCondition is true.
     *
     * @param condition expected condition
     * @param timeoutMs timeout in milliseconds
     */
    public static void waitForCondition(ExpectedCondition<?> condition, int timeoutMs) {
        new WebDriverWait(webdriver().object(), Duration.ofMillis(timeoutMs)).until(condition);
    }

    /**
     * Waits until a custom ExpectedCondition is true with default timeout.
     *
     * @param condition expected condition
     */
    public static void waitForCondition(ExpectedCondition<?> condition) {
        waitForCondition(condition, DEFAULT_TIMEOUT);
    }

    /**
     * Hard sleep (avoid using unless absolutely necessary).
     *
     * @param millis time in milliseconds
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread sleep interrupted", e);
        }
    }
}
