package com.swaglabs.tests;

import com.swaglabs.listeners.RetryAnalyzer;

import io.qameta.allure.*;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTest validates the Swag Labs app launch and login functionality.
 *
 * Test Scenarios:
 * - Verify the app launches and login page is displayed
 * - Login with valid credentials (standard_user / secret_sauce)
 * - Login with invalid credentials
 * - Login with empty credentials
 */
@Epic("Swag Labs Mobile Automation")
@Feature("Login")
public class LoginTest extends BaseTest {

    @Test(description = "TC_LOGIN_001 — Verify app launches and login page is displayed",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.BLOCKER)
    @Story("App Launch")
    @Description("Verify that the Swag Labs app launches successfully and the login page is loaded")
    public void testAppLaunchAndLoginPageDisplayed() {
        LOG.info("Verifying app launch and login page display");

        Assert.assertTrue(loginPage.isPageLoaded(),
                "Login page should be displayed after app launch");
    }

    @Test(description = "TC_LOGIN_002 — Login with valid credentials (standard_user)",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.BLOCKER)
    @Story("Valid Login")
    @Description("Login with standard_user / secret_sauce and verify navigation to Products page")
    public void testLoginWithValidCredentials() {
        LOG.info("Testing login with valid credentials: standard_user");

        loginPage.login(STANDARD_USER, PASSWORD);

        Assert.assertTrue(productsPage.isPageLoaded(),
                "Products page should be displayed after successful login");
    }

    @Test(description = "TC_LOGIN_003 — Login with invalid credentials",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Invalid Login")
    @Description("Attempt login with invalid credentials and verify error message is displayed")
    public void testLoginWithInvalidCredentials() {
        LOG.info("Testing login with invalid credentials");

        loginPage.login("invalid_user", "wrong_password");

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed for invalid credentials");
    }

    @Test(description = "TC_LOGIN_004 — Login with empty username",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid Login")
    @Description("Attempt login with empty username and verify error message")
    public void testLoginWithEmptyUsername() {
        LOG.info("Testing login with empty username");

        loginPage.enterPassword(PASSWORD).tapLoginButton();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed when username is empty");
    }

    @Test(description = "TC_LOGIN_005 — Login with empty password",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid Login")
    @Description("Attempt login with empty password and verify error message")
    public void testLoginWithEmptyPassword() {
        LOG.info("Testing login with empty password");

        loginPage.enterUsername(STANDARD_USER).tapLoginButton();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed when password is empty");
    }
}
