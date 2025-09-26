package com.ecommerce.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.ecommerce.base.BasePage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

/**
 * Page object for product detail page (product.html).
 * DemoBlaze-like structure:
 *  - product title selector: .name
 *  - price selector: .price-container
 *  - add to cart button: .btn.btn-success.btn-lg
 *  - cart navigation link: "//a[text()='Cart']"
 */
public class ProductPage extends BasePage {

    private final SelenideElement addToCartButton = $(".btn.btn-success.btn-lg");
    private final SelenideElement productTitle = $(".name");
    private final SelenideElement productPrice = $(".price-container");
    private final SelenideElement productDescription = $("#more-information"); // fallback
    private final SelenideElement cartNavLink = $x("//a[text()='Cart']");

    /**
     * Clicks Add to Cart and handles the resulting alert automatically.
     */
    public void clickAddToCart() {
        addToCartButton.shouldBe(visible).click();
        // Accept alert if it appears
        try {
            Selenide.switchTo().alert().accept();
        } catch (Exception ignored) {
            // Alert not present; continue
        }
    }

    /**
     * Clicks the Cart link in the navigation bar.
     */
    public void clickCartNav() {
        cartNavLink.shouldBe(visible).click();
    }

    public boolean isAddToCartButtonVisible() {
        return addToCartButton.shouldBe(visible).isDisplayed();
    }

    public String getProductTitle() {
        return productTitle.shouldBe(visible).getText().trim();
    }

    /**
     * Returns the price text as displayed (e.g., "$360 *includes tax").
     */
    public String getProductPrice() {
        return productPrice.shouldBe(visible).getText().trim();
    }

    public String getProductDescription() {
        return productDescription.shouldBe(visible).getText().trim();
    }
}
