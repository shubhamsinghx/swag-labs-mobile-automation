package com.swaglabs.tests;

import com.swaglabs.listeners.RetryAnalyzer;
import com.swaglabs.pages.MenuPage;

import io.qameta.allure.*;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * MenuValidationTest validates the side menu (hamburger menu) options on the Swag Labs app.
 *
 * Test Scenarios:
 * - Navigate back to home page and open the menu
 * - Validate all available menu options (ALL ITEMS, WEBVIEW, ABOUT, LOGOUT, RESET APP STATE)
 * - Verify menu close functionality
 */
@Epic("Swag Labs Mobile Automation")
@Feature("Menu Validation")
public class MenuValidationTest extends BaseTest {

    @Override
    @Parameters({"orientation"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("PORTRAIT") String orientation) {
        super.setUp(orientation);
        loginAsStandardUser();
    }

    @Test(description = "TC_MENU_001 — Open menu and validate all options are displayed",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Menu Options")
    @Description("Open the side menu from the home page and verify all expected menu options are displayed")
    public void testAllMenuOptionsDisplayed() {
        LOG.info("Opening menu and validating all options");

        Assert.assertTrue(productsPage.isPageLoaded(),
                "Products page should be loaded before opening menu");

        menuPage.openMenu();

        // Validate each menu option individually
        Assert.assertTrue(menuPage.isAllItemsDisplayed(),
                "Menu option 'ALL ITEMS' should be displayed");
        Assert.assertTrue(menuPage.isWebViewDisplayed(),
                "Menu option 'WEBVIEW' should be displayed");
        Assert.assertTrue(menuPage.isAboutDisplayed(),
                "Menu option 'ABOUT' should be displayed");
        Assert.assertTrue(menuPage.isLogoutDisplayed(),
                "Menu option 'LOGOUT' should be displayed");
        Assert.assertTrue(menuPage.isResetAppStateDisplayed(),
                "Menu option 'RESET APP STATE' should be displayed");

        LOG.info("All {} menu options validated: {}",
                MenuPage.EXPECTED_MENU_OPTIONS.size(), MenuPage.EXPECTED_MENU_OPTIONS);
    }

    @Test(description = "TC_MENU_002 — Validate all menu options collectively",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Menu Options")
    @Description("Open the side menu and verify all menu options are displayed in one assertion")
    public void testAreAllMenuOptionsDisplayedCollectively() {
        LOG.info("Validating all menu options collectively");

        menuPage.openMenu();

        Assert.assertTrue(menuPage.areAllMenuOptionsDisplayed(),
                "All expected menu options (ALL ITEMS, WEBVIEW, ABOUT, LOGOUT, RESET APP STATE) " +
                        "should be displayed");
    }

    @Test(description = "TC_MENU_003 — Close menu returns to products page",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("Menu Close")
    @Description("Open the side menu, then close it and verify the Products page is still visible")
    public void testCloseMenuReturnsToProducts() {
        LOG.info("Testing menu close functionality");

        menuPage.openMenu();
        Assert.assertTrue(menuPage.isPageLoaded(), "Menu should be open");

        menuPage.closeMenu();

        Assert.assertTrue(productsPage.isPageLoaded(),
                "Products page should be visible after closing the menu");
    }

    @Test(description = "TC_MENU_004 — Menu 'All Items' navigates back to products",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("Menu Navigation")
    @Description("Open the side menu, tap 'All Items', and verify return to Products page")
    public void testAllItemsNavigatesToProducts() {
        LOG.info("Testing 'All Items' menu navigation");

        // Navigate away from products (go to cart)
        productsPage.tapCartIcon();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should be loaded");

        // Open menu and tap All Items
        menuPage.openMenu();
        menuPage.tapAllItems();

        Assert.assertTrue(productsPage.isPageLoaded(),
                "Products page should be displayed after tapping 'All Items'");
    }

    @Test(description = "TC_MENU_005 — Menu 'Logout' returns to login page",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Logout")
    @Description("Open the side menu, tap 'Logout', and verify return to Login page")
    public void testLogoutReturnsToLoginPage() {
        LOG.info("Testing logout from menu");

        menuPage.openMenu();
        menuPage.tapLogout();

        Assert.assertTrue(loginPage.isPageLoaded(),
                "Login page should be displayed after logout");
    }

    @Test(description = "TC_MENU_006 — Validate menu after complete E2E flow and back home",
            retryAnalyzer = RetryAnalyzer.class, priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Post-Order Menu Validation")
    @Description("Complete checkout, navigate back home, open menu, and validate all options")
    public void testMenuAfterCompleteCheckoutFlowAndBackHome() {
        LOG.info("Running E2E flow then validating menu from home page");

        // Complete full checkout flow
        productsPage.addMultipleProductsToCart(2);
        productsPage.tapCartIcon();
        cartPage.tapCheckout();
        checkoutInfoPage.fillCheckoutInfo("John", "Doe", "10001");
        checkoutInfoPage.tapContinue();
        checkoutOverviewPage.tapFinish();

        // Verify order completion
        Assert.assertTrue(checkoutCompletePage.isPageLoaded(),
                "Order completion page should be displayed");

        // Navigate back to home
        checkoutCompletePage.tapBackHome();
        Assert.assertTrue(productsPage.isPageLoaded(),
                "Products page should be displayed after tapping Back Home");

        // Open menu and validate all options
        menuPage.openMenu();
        Assert.assertTrue(menuPage.areAllMenuOptionsDisplayed(),
                "All menu options should be visible after returning from checkout to home");

        LOG.info("Menu validation after E2E checkout flow passed");
    }
}
