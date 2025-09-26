package com.ecommerce.pages;



import com.ecommerce.base.BasePage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.switchTo;

public class SignupPage extends BasePage {

    // === Modal Elements ===
    private final SelenideElement signupModal = $("#signInModal");
    private final SelenideElement modalTitle = $("#signInModalLabel");

    private final SelenideElement usernameInput = $("#sign-username");
    private final SelenideElement passwordInput = $("#sign-password");

    private final SelenideElement closeButton = $("#signInModal .btn-secondary"); // "Close" button
    private final SelenideElement signupButton = $("#signInModal .btn-primary");  // "Sign up" button
    private final SelenideElement closeIcon = $("#signInModal .close");           // "X" at top right

    // === Actions ===

    /**
     * Check if the Sign up modal is displayed.
     */
    public boolean isModalDisplayed() {
        return signupModal.shouldBe(visible).isDisplayed();
    }

    /**
     * Get modal title ("Sign up").
     */
    public String getModalTitle() {
        return modalTitle.shouldBe(visible).getText();
    }

    /**
     * Enter username.
     */
    public void enterUsername(String username) {
        type(usernameInput, username);
    }

    /**
     * Enter password.
     */
    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    /**
     * Click Sign up button.
     */
    public void clickSignupButton() {
        click(signupButton);
    }

    /**
     * Click Close button at bottom.
     */
    public void clickCloseButton() {
        click(closeButton);
    }

    /**
     * Click "X" icon at top right.
     */
    public void clickCloseIcon() {
        click(closeIcon);
    }

    // === Alert handling (signup success/failure messages) ===

    /**
     * Get alert text (e.g., "Sign up successful.")
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
