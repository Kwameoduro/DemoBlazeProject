package com.ecommerce.pages;


import com.ecommerce.base.BasePage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.visible;

public class OrderPage extends BasePage {

    // === Place Order Modal ===
    private final SelenideElement orderModal = $("#orderModal");
    private final SelenideElement modalTitle = $("#orderModalLabel");
    private final SelenideElement totalLabel = $("#orderModal .modal-body #totalm"); // shows total amount

    private final SelenideElement nameInput = $("#name");
    private final SelenideElement countryInput = $("#country");
    private final SelenideElement cityInput = $("#city");
    private final SelenideElement cardInput = $("#card");
    private final SelenideElement monthInput = $("#month");
    private final SelenideElement yearInput = $("#year");

    private final SelenideElement closeButton = $("#orderModal .btn-secondary");
    private final SelenideElement purchaseButton = $("#orderModal .btn-primary");
    private final SelenideElement closeIcon = $("#orderModal .close");

    // === Success Modal ===
    private final SelenideElement successModal = $(".sweet-alert.showSweetAlert.visible");
    private final SelenideElement successTitle = $(".sweet-alert h2"); // "Thank you for your purchase!"
    private final SelenideElement successDetails = $(".sweet-alert p"); // contains ID, Amount, Card Number, Name
    private final SelenideElement okButton = $(".confirm.btn.btn-lg.btn-primary");

    // === Actions (Order Modal) ===
    public boolean isOrderModalDisplayed() {
        return orderModal.shouldBe(visible).isDisplayed();
    }

    public String getModalTitle() {
        return modalTitle.shouldBe(visible).getText();
    }

    public String getTotalAmount() {
        return totalLabel.shouldBe(visible).getText();
    }

    public void enterName(String name) {
        type(nameInput, name);
    }

    public void enterCountry(String country) {
        type(countryInput, country);
    }

    public void enterCity(String city) {
        type(cityInput, city);
    }

    public void enterCard(String card) {
        type(cardInput, card);
    }

    public void enterMonth(String month) {
        type(monthInput, month);
    }

    public void enterYear(String year) {
        type(yearInput, year);
    }

    public void clickCloseButton() {
        click(closeButton);
    }

    public void clickPurchase() {
        click(purchaseButton);
    }

    public void clickCloseIcon() {
        click(closeIcon);
    }

    // === Actions (Success Modal) ===
    public boolean isSuccessModalDisplayed() {
        return successModal.shouldBe(visible).isDisplayed();
    }

    public String getSuccessTitle() {
        return successTitle.shouldBe(visible).getText();
    }

    public String getSuccessDetails() {
        return successDetails.shouldBe(visible).getText();
    }

    public void clickOkButton() {
        click(okButton);
    }
}
