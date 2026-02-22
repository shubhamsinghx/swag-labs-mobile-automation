package com.swaglabs.pages;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * ProductsPage represents the Swag Labs product listing (inventory) screen.
 * Provides methods to browse, select, and add products to cart.
 */
public class ProductsPage extends BasePage {

    // ==================== Locators ====================
    private static final By PAGE_TITLE = AppiumBy.accessibilityId("test-PRODUCTS");
    private static final By PRODUCT_ITEMS = AppiumBy.accessibilityId("test-Item");
    private static final By CART_BADGE = AppiumBy.accessibilityId("test-Cart");
    private static final By CART_BADGE_COUNT = AppiumBy.accessibilityId("test-Cart drop zone");
    private static final By ADD_TO_CART_BUTTON = AppiumBy.accessibilityId("test-ADD TO CART");
    private static final By REMOVE_BUTTON = AppiumBy.accessibilityId("test-REMOVE");
    private static final By SORT_BUTTON = AppiumBy.accessibilityId("test-Modal Selector Button");
    private static final By PRODUCT_TITLE = AppiumBy.accessibilityId("test-Item title");
    private static final By PRODUCT_PRICE = AppiumBy.accessibilityId("test-Price");

    // ==================== Page Actions ====================

    @Step("Get all product items on the page")
    public List<WebElement> getAllProducts() {
        return findAll(PRODUCT_ITEMS);
    }

    @Step("Get product count")
    public int getProductCount() {
        return getAllProducts().size();
    }

    @Step("Get all product titles")
    public List<String> getAllProductTitles() {
        List<WebElement> titles = findAll(PRODUCT_TITLE);
        return titles.stream().map(WebElement::getText).toList();
    }

    @Step("Tap on product at index: {index}")
    public void tapProductAtIndex(int index) {
        List<WebElement> products = getAllProducts();
        if (index >= 0 && index < products.size()) {
            products.get(index).click();
        } else {
            throw new IndexOutOfBoundsException(
                    "Product index " + index + " out of range. Total products: " + products.size());
        }
    }

    @Step("Tap on product by name: {productName}")
    public void tapProductByName(String productName) {
        scrollToText(productName).click();
    }

    @Step("Add product to cart at index: {index}")
    public ProductsPage addProductToCartAtIndex(int index) {
        List<WebElement> addButtons = findAll(ADD_TO_CART_BUTTON);
        if (index >= 0 && index < addButtons.size()) {
            addButtons.get(index).click();
        } else {
            throw new IndexOutOfBoundsException(
                    "Add-to-cart button index " + index + " out of range. Total: " + addButtons.size());
        }
        return this;
    }

    @Step("Add first {count} products to cart")
    public ProductsPage addMultipleProductsToCart(int count) {
        for (int i = 0; i < count; i++) {
            List<WebElement> addButtons = driver().findElements(ADD_TO_CART_BUTTON);
            if (!addButtons.isEmpty()) {
                addButtons.get(0).click();
            } else {
                // Scroll down and try again
                scrollDown();
                addButtons = driver().findElements(ADD_TO_CART_BUTTON);
                if (!addButtons.isEmpty()) {
                    addButtons.get(0).click();
                }
            }
        }
        return this;
    }

    @Step("Navigate to cart")
    public void tapCartIcon() {
        tap(CART_BADGE);
    }

    @Step("Get cart badge count")
    public String getCartBadgeCount() {
        try {
            WebElement badge = driver().findElement(
                    By.xpath("//android.view.ViewGroup[@content-desc='test-Cart']/android.view.ViewGroup/android.widget.TextView"));
            return badge.getText();
        } catch (Exception e) {
            return "0";
        }
    }

    @Step("Tap sort button")
    public void tapSortButton() {
        tap(SORT_BUTTON);
    }

    @Step("Check if Products page is loaded")
    @Override
    public boolean isPageLoaded() {
        return isDisplayed(PAGE_TITLE);
    }
}
