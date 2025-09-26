package com.ecommerce.base;





import com.codeborne.selenide.SelenideElement;
import com.ecommerce.utils.ConfigReader;
import com.ecommerce.utils.WaitUtils;

import static com.codeborne.selenide.Selenide.*;

/**
 * BasePage provides reusable methods for all Page Object classes.
 * All POM classes should extend this class.
 */
public abstract class BasePage {

    /**
     * Navigate to a specific path relative to the base URL.
     *
     * @param path relative path (e.g., "/cart.html")
     */
    protected void navigateTo(String path) {
        open(ConfigReader.getBaseUrl() + path);
    }

    /**
     * Open the base URL (homepage).
     */
    public void openBaseUrl() {
        open(ConfigReader.getBaseUrl());
    }

    /**
     * Click an element after ensuring it is visible.
     *
     * @param element the SelenideElement to click
     */
    protected void click(SelenideElement element) {
        WaitUtils.waitForVisibility(element);
        element.click();
    }

    /**
     * Type text into an input field after clearing it.
     *
     * @param element the SelenideElement input field
     * @param text    the text to enter
     */
    protected void type(SelenideElement element, String text) {
        WaitUtils.waitForVisibility(element);
        element.clear();
        element.setValue(text);
    }

    /**
     * Get the text of an element.
     *
     * @param element the SelenideElement
     * @return element text
     */
    protected String getText(SelenideElement element) {
        WaitUtils.waitForVisibility(element);
        return element.getText();
    }

    /**
     * Check if an element is visible.
     *
     * @param element the SelenideElement
     * @return true if visible, false otherwise
     */
    protected boolean isVisible(SelenideElement element) {
        try {
            WaitUtils.waitForVisibility(element);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait until an element contains specific text.
     *
     * @param element the SelenideElement
     * @param text    the expected text
     */
    protected void waitForText(SelenideElement element, String text) {
        WaitUtils.waitForText(element, text);
    }

    /**
     * Wait until an element disappears from the page.
     *
     * @param element the SelenideElement
     */
    protected void waitForInvisibility(SelenideElement element) {
        WaitUtils.waitForInvisibility(element);
    }
}
