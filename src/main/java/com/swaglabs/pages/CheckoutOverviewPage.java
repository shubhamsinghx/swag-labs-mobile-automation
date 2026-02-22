package com.swaglabs.pages;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * CheckoutOverviewPage represents the Swag Labs checkout overview screen.
 * Displays order summary, payment info, and total before completing the purchase.
 */
public class CheckoutOverviewPage extends BasePage {

    // ==================== Locators ====================
    private static final By FINISH_BUTTON = AppiumBy.accessibilityId("test-FINISH");
    private static final By CANCEL_BUTTON = AppiumBy.accessibilityId("test-CANCEL");
    private static final By ITEM_TOTAL = AppiumBy.accessibilityId("test-Item total");
    private static final By TAX_TOTAL = AppiumBy.accessibilityId("test-Tax");
    private static final By TOTAL_PRICE = AppiumBy.accessibilityId("test-Total");
    private static final By PAYMENT_INFO = AppiumBy.accessibilityId("test-Payment Info");
    private static final By SHIPPING_INFO = AppiumBy.accessibilityId("test-Shipping Info");
    private static final By CART_ITEMS = AppiumBy.accessibilityId("test-Item");
    private static final By ITEM_TITLE = AppiumBy.accessibilityId("test-Item title");
    private static final By ITEM_PRICE = AppiumBy.accessibilityId("test-Price");

    // ==================== Page Actions ====================

    @Step("Get items in checkout overview")
    public List<WebElement> getOrderItems() {
        return findAll(CART_ITEMS);
    }

    @Step("Get order item count")
    public int getOrderItemCount() {
        return getOrderItems().size();
    }

    @Step("Get order item titles")
    public List<String> getOrderItemTitles() {
        return findAll(ITEM_TITLE).stream().map(WebElement::getText).toList();
    }

    @Step("Get payment information")
    public String getPaymentInfo() {
        scrollDown();
        return getText(PAYMENT_INFO);
    }

    @Step("Get shipping information")
    public String getShippingInfo() {
        return getText(SHIPPING_INFO);
    }

    @Step("Get item total")
    public String getItemTotal() {
        scrollDown();
        return getText(ITEM_TOTAL);
    }

    @Step("Get tax amount")
    public String getTax() {
        return getText(TAX_TOTAL);
    }

    @Step("Get total price")
    public String getTotalPrice() {
        scrollDown();
        return getText(TOTAL_PRICE);
    }

    @Step("Tap Finish button to complete order")
    public void tapFinish() {
        scrollDown();
        tap(FINISH_BUTTON);
    }

    @Step("Tap Cancel button")
    public void tapCancel() {
        tap(CANCEL_BUTTON);
    }

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(FINISH_BUTTON);
    }
}
