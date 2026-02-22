package com.swaglabs.pages;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;

import org.openqa.selenium.By;

/**
 * CheckoutCompletePage represents the Swag Labs order confirmation screen.
 * Displays the order completion message after a successful checkout.
 */
public class CheckoutCompletePage extends BasePage {

    // ==================== Locators ====================
    private static final By COMPLETE_CONTAINER = AppiumBy.accessibilityId("test-CHECKOUT: COMPLETE!");
    private static final By COMPLETE_HEADER = AppiumBy.xpath(
            "//android.widget.ScrollView[@content-desc='test-CHECKOUT: COMPLETE!']" +
                    "//android.widget.TextView[@text='THANK YOU FOR YOU ORDER']");
    private static final By COMPLETE_TEXT = AppiumBy.xpath(
            "//android.widget.ScrollView[@content-desc='test-CHECKOUT: COMPLETE!']" +
                    "//android.widget.TextView[contains(@text, 'Your order has been dispatched')]");
    private static final By BACK_HOME_BUTTON = AppiumBy.accessibilityId("test-BACK HOME");
    private static final By PONY_EXPRESS_IMAGE = AppiumBy.xpath(
            "//android.widget.ScrollView[@content-desc='test-CHECKOUT: COMPLETE!']" +
                    "//android.widget.ImageView");

    // ==================== Page Actions ====================

    @Step("Verify order completion header is displayed")
    public boolean isOrderCompleteHeaderDisplayed() {
        return isDisplayed(COMPLETE_CONTAINER);
    }

    @Step("Get order completion header text")
    public String getCompleteHeaderText() {
        return getText(COMPLETE_HEADER);
    }

    @Step("Get order completion description text")
    public String getCompleteDescriptionText() {
        return getText(COMPLETE_TEXT);
    }

    @Step("Verify order completion page elements are displayed")
    public boolean isOrderCompletionValid() {
        return isDisplayed(COMPLETE_CONTAINER) && isDisplayed(BACK_HOME_BUTTON);
    }

    @Step("Verify pony express image is displayed")
    public boolean isPonyExpressImageDisplayed() {
        return isDisplayed(PONY_EXPRESS_IMAGE);
    }

    @Step("Tap Back Home button")
    public void tapBackHome() {
        tap(BACK_HOME_BUTTON);
    }

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(COMPLETE_CONTAINER);
    }
}
