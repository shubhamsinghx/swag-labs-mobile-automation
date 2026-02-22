package com.swaglabs.pages;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;

import org.openqa.selenium.By;

/**
 * CheckoutInfoPage represents the Swag Labs checkout information screen.
 * Users enter first name, last name, and zip/postal code here.
 */
public class CheckoutInfoPage extends BasePage {

    // ==================== Locators ====================
    private static final By FIRST_NAME_FIELD = AppiumBy.accessibilityId("test-First Name");
    private static final By LAST_NAME_FIELD = AppiumBy.accessibilityId("test-Last Name");
    private static final By ZIP_CODE_FIELD = AppiumBy.accessibilityId("test-Zip/Postal Code");
    private static final By CONTINUE_BUTTON = AppiumBy.accessibilityId("test-CONTINUE");
    private static final By CANCEL_BUTTON = AppiumBy.accessibilityId("test-CANCEL");
    private static final By ERROR_MESSAGE = AppiumBy.accessibilityId("test-Error message");

    // ==================== Page Actions ====================

    @Step("Enter first name: {firstName}")
    public CheckoutInfoPage enterFirstName(String firstName) {
        type(FIRST_NAME_FIELD, firstName);
        return this;
    }

    @Step("Enter last name: {lastName}")
    public CheckoutInfoPage enterLastName(String lastName) {
        type(LAST_NAME_FIELD, lastName);
        return this;
    }

    @Step("Enter zip code: {zipCode}")
    public CheckoutInfoPage enterZipCode(String zipCode) {
        type(ZIP_CODE_FIELD, zipCode);
        return this;
    }

    /**
     * Fills in all checkout information fields.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @param zipCode   the zip/postal code
     */
    @Step("Fill checkout info — First: {firstName}, Last: {lastName}, Zip: {zipCode}")
    public CheckoutInfoPage fillCheckoutInfo(String firstName, String lastName, String zipCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterZipCode(zipCode);
        return this;
    }

    @Step("Tap Continue button")
    public void tapContinue() {
        tap(CONTINUE_BUTTON);
    }

    @Step("Tap Cancel button")
    public void tapCancel() {
        tap(CANCEL_BUTTON);
    }

    @Step("Get checkout info error message")
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(FIRST_NAME_FIELD) && isDisplayed(CONTINUE_BUTTON);
    }
}
