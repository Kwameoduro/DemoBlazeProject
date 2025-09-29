package com.ecommerce.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.ecommerce.base.BasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Condition.visible;

public class CartPage extends BasePage {

    // === Table Elements ===
    private final ElementsCollection productRows = $$("#tbodyid tr");
    private final ElementsCollection productImages = $$("#tbodyid tr td img");
    private final ElementsCollection productTitles = $$("#tbodyid tr td:nth-child(2)");
    private final ElementsCollection productPrices = $$("#tbodyid tr td:nth-child(3)");
    private final ElementsCollection deleteButtons = $$("#tbodyid tr td:nth-child(4) a");
    private final SelenideElement cartTable = $("#tbodyid");
    private final ElementsCollection cartRows = $$("#tbodyid tr"); // all rows in cart table


    // === Total and Order ===
    private final SelenideElement totalPrice = $("#totalp");
    private final SelenideElement placeOrderButton = $(".btn.btn-success");

    // === Actions ===

    /** Get number of products in the cart. */
    public int getProductCount() {
        return productRows.size();
    }

    /** Get product title at a specific row. */
    public String getProductTitle(int index) {
        return productTitles.get(index).shouldBe(visible).getText().trim();
    }

    /** ✅ Alias for clarity: Get product name at a specific row. */
    public String getProductName(int index) {
        return getProductTitle(index);
    }

    /** Get product price at a specific row. */
    public String getProductPrice(int index) {
        return productPrices.get(index).shouldBe(visible).getText().trim();
    }

    /** Check if product image is displayed at a specific row. */
    public boolean isProductImageDisplayed(int index) {
        return productImages.get(index).shouldBe(visible).isDisplayed();
    }


    /** Get total price of all items in the cart. */
    public String getTotalPrice() {
        return totalPrice.shouldBe(visible).getText().trim();
    }

    /** Click Place Order button. */
    public void clickPlaceOrder() {
        click(placeOrderButton);
    }

    /** Check if Place Order button is visible. */
    public boolean isPlaceOrderButtonVisible() {
        return placeOrderButton.shouldBe(visible).isDisplayed();
    }

    /** Check if cart table is visible. */
    public boolean isCartTableVisible() {
        return cartTable.shouldBe(visible).isDisplayed();
    }

    /** Check if Total Price field is visible. */
    public boolean isTotalPriceFieldVisible() {
        return totalPrice.shouldBe(visible).isDisplayed();
    }
    public boolean isCartEmpty() {
        return $$("#tbodyid tr").isEmpty(); // re-query the DOM
    }

    public void deleteProduct(int index) {
        deleteButtons.get(index).shouldBe(visible).click();

        // Wait until either cart is empty or rows are fewer
        $$("#tbodyid tr").shouldBe(CollectionCondition.sizeLessThanOrEqual(0));
    }

    public boolean isTheTotalPriceFieldVisible() {
        return $("#totalp").shouldBe(Condition.visible).exists();
    }

    public void deleteFirstProduct() {
        $("#tbodyid tr td:nth-child(3) a").shouldBe(Condition.visible).click();
    }

    /** Wait until cart table is loaded with at least one row */
    public void waitForCartToLoad() {
        cartRows.shouldBe(CollectionCondition.sizeGreaterThan(0));
    }

    /** Wait until a product disappears after deletion */
    public void waitForCartToUpdate(int previousCount) {
        cartRows.shouldHave(CollectionCondition.sizeLessThan(previousCount));
    }

    /** Check if product is in cart */
    public boolean isProductInCart(String productName) {
        return productTitles.texts().contains(productName);
    }

}
