package com.swaglabs.pages;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;

import org.openqa.selenium.By;

/**
 * LoginPage represents the Swag Labs login screen.
 * Provides methods to interact with username, password fields and login button.
 */
public class LoginPage extends BasePage {

    // ==================== Locators ====================
    private static final By USERNAME_FIELD = AppiumBy.accessibilityId("test-Username");
    private static final By PASSWORD_FIELD = AppiumBy.accessibilityId("test-Password");
    private static final By LOGIN_BUTTON = AppiumBy.accessibilityId("test-LOGIN");
    private static final By ERROR_MESSAGE = AppiumBy.accessibilityId("test-Error message");

    // ==================== Page Actions ====================

    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        type(USERNAME_FIELD, username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        type(PASSWORD_FIELD, password);
        return this;
    }

    @Step("Tap Login button")
    public void tapLoginButton() {
        tap(LOGIN_BUTTON);
    }

    /**
     * Performs complete login with the provided credentials.
     *
     * @param username the username to enter
     * @param password the password to enter
     */
    @Step("Login with username: {username}")
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        tapLoginButton();
    }

    /**
     * Returns the error message text displayed on failed login.
     */
    @Step("Get login error message")
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    /**
     * Checks if the error message is displayed.
     */
    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(USERNAME_FIELD) && isDisplayed(LOGIN_BUTTON);
    }
}
