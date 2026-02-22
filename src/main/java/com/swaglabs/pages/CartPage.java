package com.swaglabs.pages;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * CartPage represents the Swag Labs shopping cart screen.
 * Provides methods to view cart items, remove items, and proceed to checkout.
 */
public class CartPage extends BasePage {

    // ==================== Locators ====================
    private static final By CART_HEADER = By.xpath("//android.widget.TextView[@text='YOUR CART']");
    private static final By CART_ITEMS = AppiumBy.accessibilityId("test-Item");
    private static final By CHECKOUT_BUTTON = AppiumBy.accessibilityId("test-CHECKOUT");
    private static final By CONTINUE_SHOPPING = AppiumBy.accessibilityId("test-CONTINUE SHOPPING");
    private static final By REMOVE_BUTTON = AppiumBy.accessibilityId("test-REMOVE");
    private static final By ITEM_TITLE = AppiumBy.accessibilityId("test-Item title");
    private static final By ITEM_DESCRIPTION = AppiumBy.accessibilityId("test-Description");
    private static final By ITEM_PRICE = AppiumBy.accessibilityId("test-Price");
    private static final By CART_QUANTITY = AppiumBy.accessibilityId("test-Amount");

    // ==================== Page Actions ====================

    @Step("Get all cart items")
    public List<WebElement> getCartItems() {
        return findAll(CART_ITEMS);
    }

    @Step("Get cart item count")
    public int getCartItemCount() {
        return getCartItems().size();
    }

    @Step("Get all item titles in cart")
    public List<String> getCartItemTitles() {
        List<WebElement> titles = findAll(ITEM_TITLE);
        return titles.stream().map(WebElement::getText).toList();
    }

    @Step("Remove item at index: {index}")
    public CartPage removeItemAtIndex(int index) {
        List<WebElement> removeButtons = findAll(REMOVE_BUTTON);
        if (index >= 0 && index < removeButtons.size()) {
            removeButtons.get(index).click();
        }
        return this;
    }

    @Step("Proceed to checkout")
    public void tapCheckout() {
        scrollDown();
        tap(CHECKOUT_BUTTON);
    }

    @Step("Continue shopping")
    public void tapContinueShopping() {
        tap(CONTINUE_SHOPPING);
    }

    @Step("Check if cart has items")
    public boolean hasItems() {
        try {
            return !driver().findElements(CART_ITEMS).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(CART_HEADER);
    }
}
