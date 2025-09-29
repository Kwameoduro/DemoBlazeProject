package com.ecommerce.pages;






import com.ecommerce.base.BasePage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.Condition.visible;

public class ContactPage extends BasePage {

    // === Modal elements ===
    private final SelenideElement contactModal = $("#exampleModal");
    private final SelenideElement modalTitle = $("#exampleModalLabel");

    private final SelenideElement emailInput = $("#recipient-email");
    private final SelenideElement nameInput = $("#recipient-name");
    private final SelenideElement messageBox = $("#message-text");

    private final SelenideElement closeButton = $("#exampleModal .btn-secondary");  // "Close" button
    private final SelenideElement sendMessageButton = $("#exampleModal .btn-primary");  // "Send message" button
    private final SelenideElement closeIcon = $("#exampleModal .close"); // "X" top-right button

    // === Actions ===

    /**
     * Check if the Contact modal is displayed.
     */
    public boolean isModalDisplayed() {
        return contactModal.shouldBe(visible).isDisplayed();
    }

    /**
     * Get modal title text ("New message").
     */
    public String getModalTitle() {
        return modalTitle.shouldBe(visible).getText();
    }

    /**
     * Enter contact email.
     */
    public void enterEmail(String email) {
        type(emailInput, email);
    }

    /**
     * Enter contact name.
     */
    public void enterName(String name) {
        type(nameInput, name);
    }

    /**
     * Enter message text.
     */
    public void enterMessage(String message) {
        type(messageBox, message);
    }

    /**
     * Click "Send message" button.
     */
    public void clickSendMessage() {
        click(sendMessageButton);
    }

    /**
     * Click "Close" button at the bottom.
     */
    public void clickCloseButton() {
        click(closeButton);
    }

    /**
     * Click "X" icon at the top right.
     */
    public void clickCloseIcon() {
        click(closeIcon);
    }

    // === Alert handling (after sending message) ===

    /**
     * Get alert text (e.g., "Thanks for the message!!").
     */
    public String getAlertText() {
        return switchTo().alert().getText();
    }

    /**
     * Accept alert (click OK).
     */
    public void acceptAlert() {
        switchTo().alert().accept();
    }

}
