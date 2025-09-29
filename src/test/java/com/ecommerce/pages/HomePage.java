package com.ecommerce.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.ecommerce.base.BasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class HomePage extends BasePage {

    // === Navbar links ===
    private final SelenideElement homeLink = $("#navbarExample a.nav-link:contains('Home')");
    private final SelenideElement aboutUsLink = $x("//a[contains(normalize-space(.),'About us')]");
    private final SelenideElement cartLink = $x("//a[text()='Cart']");
    private final SelenideElement loginLink = $("#login2");
    private final SelenideElement signupLink = $("#signin2");
    private final SelenideElement logoutLink = $("#logout2");
    private final SelenideElement welcomeUser = $("#nameofuser"); // "Welcome <username>"
    private final SelenideElement contactLink = $("a.nav-link[data-target='#exampleModal']");




    // === Slider ===
    private final SelenideElement slider = $("#carouselExampleIndicators");
    private final ElementsCollection sliderImages = $$("#carouselExampleIndicators .carousel-item img");
    private final SelenideElement prevSlideButton = $("#carouselExampleIndicators .carousel-control-prev");
    private final SelenideElement nextSlideButton = $("#carouselExampleIndicators .carousel-control-next");

    // === Categories ===
    private final SelenideElement categoriesSection = $("#cat");
    private final SelenideElement phonesCategory = $x("//a[contains(text(),'Phones')]");
    private final SelenideElement laptopsCategory = $x("//a[contains(text(),'Laptops')]");
    private final SelenideElement monitorsCategory = $x("//a[contains(text(),'Monitors')]");

    // === Products ===
    private final ElementsCollection productCards = $$(".card-block");
    private final ElementsCollection productTitles = $$(".card-title a");
    private final ElementsCollection productPrices = $$(".card-block h5");

    // === Pagination ===
    private final SelenideElement nextButton = $("#next2");
    private final SelenideElement prevButton = $("#prev2");

    private final SelenideElement firstProductLink = $("#tbodyid .hrefch");
    private final SelenideElement addToCartButton = $(".btn.btn-success");

    // === Navbar actions ===
    public ElementsCollection navLinks() {
        return $$("#navbarExample a.nav-link");
    }

    public void clickHome() {
        click(homeLink);
    }

    public void clickContact() {
        click(contactLink);
    }

    public void clickAboutUs() {
        navLinks().findBy(text("About us"))
                .shouldBe(visible, Duration.ofSeconds(10))
                .click();
    }


    public void clickLogin() {
        loginLink.shouldBe(visible, Duration.ofSeconds(10)).click();
    }

    public void clickSignup() {
        click(signupLink);
    }

    public void clickLogout() {
        click(logoutLink);
    }

    // === User state ===
    public boolean isLoginVisible() {
        return isVisible(loginLink);
    }

    public boolean isSignupVisible() {
        return isVisible(signupLink);
    }

    public boolean isLogoutVisible() {
        return isVisible(logoutLink);
    }

    public boolean isUserLoggedIn(String username) {
        return welcomeUser.shouldBe(visible)
                .has(text("Welcome " + username))
                && isLogoutVisible();
    }

    public boolean isUserLoggedOut() {
        return isLoginVisible() && isSignupVisible();
    }

    // === Slider ===
    public boolean isSliderDisplayed() {
        return isVisible(slider);
    }

    public int getSliderImageCount() {
        return sliderImages.size();
    }

    public void clickNextSlide() {
        click(nextSlideButton);
    }

    public void clickPrevSlide() {
        click(prevSlideButton);
    }

    // === Categories ===
    public boolean isCategoriesVisible() {
        return isVisible(categoriesSection);
    }

    public void selectPhonesCategoryAndWait() {
        phonesCategory.scrollTo().click();
        waitForProductsToLoad();
    }

    public void selectLaptopsCategoryAndWait() {
        laptopsCategory.scrollTo().click();
        waitForProductsToLoad();
    }

    public void selectMonitorsCategoryAndWait() {
        monitorsCategory.scrollTo().click();
        waitForProductsToLoad();
    }

    // === Products ===
    public int getProductCount() {
        return productCards.size();
    }

    public String getProductTitle(int index) {
        return getText(productTitles.get(index));
    }

    public String getProductPrice(int index) {
        return getText(productPrices.get(index));
    }


    public ElementsCollection productCards() {
        return $$(".card-block");
    }

    public void waitForProductsToLoad() {
        productCards().first().shouldBe(visible, Duration.ofSeconds(10));
    }

    // === Pagination ===
    public void goToNextPage() {
        click(nextButton);
    }

    public void goToPreviousPage() {
        click(prevButton);
    }

    public boolean isNextButtonVisible() {
        return isVisible(nextButton);
    }


    public boolean isPrevButtonVisible() {
        return prevButton.isDisplayed();   // checks rendered visibility
    }

    public void waitForFirstProductChange(String previousFirstTitle) {
        $$(".card-title a").first()
                .shouldNotHave(text(previousFirstTitle), Duration.ofSeconds(10));
    }

    public void goToNextPageAndWait(String previousFirstTitle) {
        nextButton.scrollIntoView(true).click();
        $$(".card-title a").first()
                .shouldNotHave(text(previousFirstTitle), Duration.ofSeconds(10));
    }

    public void goToPreviousPageAndWait(String previousFirstTitle) {
        prevButton.scrollIntoView(true).click();
        $$(".card-title a").first()
                .shouldNotHave(text(previousFirstTitle), Duration.ofSeconds(10));
    }

    public List<String> getAllProductTitles() {
        $$("#tbodyid .card-title").shouldHave(sizeGreaterThan(0));

        return $$("#tbodyid .card-title").texts();
    }

    public void clickCart() {
        click(cartLink);
    }

    public void addFirstProductToCart() {
        click(firstProductLink);
        click(addToCartButton);
    }

    public void acceptAlert() {
        com.codeborne.selenide.Selenide.switchTo().alert().accept();
    }

    // HomePage.java
    public void clickProductByName(String productName) {
        $$("#tbodyid .hrefch")
                .findBy(Condition.text(productName))
                .shouldBe(Condition.visible)
                .click();
    }



    // Existing method — keep for backward compatibility
    public void clickOnProduct(int index) {
        click(productTitles.get(index));
    }

    // New method — click by product name
    public void clickOnProduct(String productName) {
        productTitles.findBy(text(productName))
                .shouldBe(visible, Duration.ofSeconds(10))
                .click();
    }

    public boolean isHomePageDisplayed() {
        // Replace "#contcont" with a unique CSS selector or ID that only exists on the homepage
        return $("#contcont").shouldBe(Condition.visible).exists();
    }

    public List<String> getCategories() {
        return $$("#itemc").texts();
        // All categories in the sidebar have id="itemc" in Demoblaze
    }


    public String getProductThumbnail(int index) {
        // Each product card contains an <img> element
        return productCards.get(index).$("img").getAttribute("src");
    }

    private final String productLink = "#tbodyid .hrefch";
    private final String addToCartButtonn = "a.btn.btn-success.btn-lg"; // "Add to cart" button

    // Click product by name (like "Samsung galaxy s6")
    public void clickProductByNamee(String productName) {
        $$(productLink).findBy(text(productName)).click();
    }

    // Add product to cart (works after opening product details page)
    public void addFirstProductToCartt() {
        $(addToCartButtonn).click();
    }

    // Accept alert
    public void acceptAlertt() {
        switchTo().alert().accept();
    }

    // Navigate to Cart
    public void clickCartt() {
        $("a#cartur").click();
    }

}
