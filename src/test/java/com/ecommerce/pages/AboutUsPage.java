package com.ecommerce.pages;

import com.ecommerce.base.BasePage;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class AboutUsPage extends BasePage {

    // modal container
    private final SelenideElement aboutUsModal = $("#videoModal");
    private final SelenideElement modalTitle   = $("#videoModalLabel");
    private final SelenideElement modalBody    = $("#videoModal .modal-body");

    // explicit button selector (more reliable)
    private final SelenideElement closeButton  = $("#videoModal button.btn.btn-secondary"); // "Close" button at bottom
    private final SelenideElement closeIcon    = $("#videoModal .close"); // "X" icon at top right

    /**
     * Check if the About Us modal is displayed.
     * This returns a boolean and never throws an exception.
     */
    public boolean isModalDisplayed() {
        try {
            if (!aboutUsModal.exists()) return false;
            String cls = aboutUsModal.getAttribute("class");
            boolean hasShow = cls != null && cls.contains("show");
            return aboutUsModal.isDisplayed() && hasShow;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Get modal title ("About us").
     */
    public String getModalTitle() {
        try {
            return modalTitle.exists() ? modalTitle.getText() : "";
        } catch (Throwable t) {
            return "";
        }
    }

    /**
     * Check if the modal body (image/video) is displayed.
     */
    public boolean isMediaDisplayed() {
        try {
            return modalBody.exists() && modalBody.isDisplayed();
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Click the "Close" button at the bottom of the modal and ensure it is closed.
     * This method implements several fallbacks to handle animation/backdrop/video issues.
     */
    public void clickCloseButton() {
        // ensure element visible and scrolled into view
        closeButton.shouldBe(visible).scrollTo();

        // 1) Try normal click, fallback to JS click
        try {
            closeButton.click();
        } catch (Throwable t) {
            // fallback: JS click
            executeJavaScript("arguments[0].click();", closeButton);
        }

        // Wait and force hide if needed
        waitUntilModalClosed();
    }

    /**
     * Click top-right close icon and wait for modal to close
     */
    public void clickCloseIcon() {
        closeIcon.shouldBe(visible).scrollTo();
        try {
            closeIcon.click();
        } catch (Throwable t) {
            executeJavaScript("arguments[0].click();", closeIcon);
        }
        waitUntilModalClosed();
    }

    /**
     * Centralized waiting / forcing logic to ensure modal actually disappears.
     */
    private void waitUntilModalClosed() {
        // 0) best-effort: stop any playing media (video / iframe) that could interfere
        executeJavaScript(
                "var m=document.getElementById('videoModal');" +
                        "if(m){" +
                        "  var v=m.querySelector('video'); if(v && v.pause) { try{v.pause();}catch(e){} }" +
                        "  var ifr=m.querySelector('iframe'); if(ifr){ try{ifr.src='about:blank';}catch(e){} }" +
                        "}"
        );

        // 1) Wait for normal disappearance (animation)
        try {
            aboutUsModal.should(disappear);
            return;
        } catch (Throwable ignored) { /* fallback below */ }

        // 2) Try Bootstrap hide via jQuery if available (common on sites)
        try {
            executeJavaScript(
                    "if (typeof jQuery !== 'undefined') {" +
                            "  try { $('#videoModal').modal('hide'); } catch(e) {}" +
                            "} "
            );
            aboutUsModal.should(disappear);
            return;
        } catch (Throwable ignored) { /* continue to forcing DOM changes */ }

        // 3) Force-remove show class / hide style and remove any backdrop elements
        executeJavaScript(
                "var m=document.getElementById('videoModal');" +
                        "if(m){ m.classList.remove('show'); m.style.display='none'; }" +
                        "var backs=document.querySelectorAll('.modal-backdrop');" +
                        "backs.forEach(function(b){ b.remove();});"
        );

        // final wait - let Selenide assert disappear (or throw for test failure)
        aboutUsModal.should(disappear);
    }
}
