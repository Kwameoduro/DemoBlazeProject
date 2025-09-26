package com.ecommerce.pages;



import com.ecommerce.base.BasePage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.Condition.visible;

public class LoginPage extends BasePage {

    // === Login Modal Elements ===
    private final SelenideElement loginModal = $("#logInModal");
    private final SelenideElement modalTitle = $("#logInModalLabel");

    private final SelenideElement usernameInput = $("#loginusername");
    private final SelenideElement passwordInput = $("#loginpassword");

    private final SelenideElement closeButton = $("#logInModal .btn-secondary"); // "Close"
    private final SelenideElement loginButton = $("#logInModal .btn-primary");   // "Log in"
    private final SelenideElement closeIcon = $("#logInModal .close");           // "X"



    // === Navbar elements after login ===
    private final SelenideElement welcomeUser = $("#nameofuser"); // e.g., "Welcome testuser"
    private final SelenideElement logoutButton = $("#logout2");

    // === Actions (Login Modal) ===

    /**
     * Check if the Login modal is displayed.
     */
    public boolean isModalDisplayed() {
        return loginModal.shouldBe(visible).isDisplayed();
    }

    /**
     * Get modal title ("Log in").
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
     * Click the Log in button.
     */
    public void clickLoginButton() {
        click(loginButton);
    }

    /**
     * Click the Close button at bottom.
     */
    public void clickCloseButton() {
        click(closeButton);
    }

    /**
     * Click the "X" icon at top right.
     */
    public void clickCloseIcon() {
        click(closeIcon);
    }

    // === Alert Handling (Invalid login) ===

    /**
     * Get alert text (e.g., "User does not exist." or "Wrong password.").
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

    // === Navbar Validation (Post-login) ===

    /**
     * Get welcome message text (e.g., "Welcome testuser").
     */
    public String getWelcomeMessage() {
        return welcomeUser.shouldBe(visible).getText();
    }

    /**
     * Check if logout button is visible.
     */
    public boolean isLogoutVisible() {
        return logoutButton.shouldBe(visible).isDisplayed();
    }

    /**
     * Click logout button.
     */
    public void clickLogout() {
        click(logoutButton);
    }




}
