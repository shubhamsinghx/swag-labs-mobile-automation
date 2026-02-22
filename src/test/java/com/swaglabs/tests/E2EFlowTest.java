package com.swaglabs.tests;

import com.swaglabs.listeners.RetryAnalyzer;
import com.swaglabs.pages.MenuPage;

import io.qameta.allure.*;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * E2EFlowTest executes the complete Swag Labs user journey as one continuous test.
 *
 * Flow:
 *   1. Launch app
 *   2. Login (standard_user / secret_sauce)
 *   3. Browse products and add first two items to cart
 *   4. Open cart, scroll down, tap Checkout
 *   5. Fill address details (first name, last name, zip) → Continue
 *   6. Review order overview → Finish
 *   7. Validate order completion
 *   8. Go Back Home and validate all menu options
 */
@Epic("Swag Labs Mobile Automation")
@Feature("End-to-End Flow")
public class E2EFlowTest extends BaseTest {

    // Uses parent's resetApp() — terminateApp + activateApp ensures login screen.
    // Runs only once since this class has a single @Test method.

    @Test(description = "E2E — Complete Swag Labs flow: Login → Browse → Cart → Checkout → Order → Menu",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.BLOCKER)
    @Story("Complete User Journey")
    @Description("Single end-to-end test covering all assignment scenarios in one continuous flow")
    public void testCompleteEndToEndFlow() {

        // ========== STEP 1: Verify app launched ==========
        LOG.info("Step 1: Verifying app launched and login page is displayed");
        Assert.assertTrue(loginPage.isPageLoaded(),
                "Login page should be displayed after app launch");

        // ========== STEP 2: Login with standard_user ==========
        LOG.info("Step 2: Logging in with standard_user / secret_sauce");
        loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(productsPage.isPageLoaded(),
                "Products page should be displayed after login");

        // ========== STEP 3: Browse and add first two products to cart ==========
        LOG.info("Step 3: Adding first two products to cart");
        productsPage.addProductToCartAtIndex(0);
        productsPage.addProductToCartAtIndex(0);  // index 0 again: first product's button is now REMOVE

        String badgeCount = productsPage.getCartBadgeCount();
        LOG.info("Cart badge shows: {}", badgeCount);
        Assert.assertEquals(badgeCount, "2", "Cart badge should show 2 items");

        // ========== STEP 4: Open cart and proceed to checkout ==========
        LOG.info("Step 4: Opening cart and proceeding to checkout");
        productsPage.tapCartIcon();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should be loaded");

        int cartItemCount = cartPage.getCartItemCount();
        LOG.info("Cart contains {} items", cartItemCount);
        Assert.assertEquals(cartItemCount, 2, "Cart should contain 2 items");

        cartPage.tapCheckout();

        // ========== STEP 5: Fill checkout details ==========
        LOG.info("Step 5: Filling checkout details — First: John, Last: Doe, Zip: 10001");
        Assert.assertTrue(checkoutInfoPage.isPageLoaded(),
                "Checkout info page should be displayed");

        checkoutInfoPage.fillCheckoutInfo("John", "Doe", "10001");
        checkoutInfoPage.tapContinue();

        // ========== STEP 6: Review order and finish ==========
        LOG.info("Step 6: Reviewing order overview and tapping Finish");
        Assert.assertTrue(checkoutOverviewPage.isPageLoaded(),
                "Checkout overview should be displayed");

        int orderItems = checkoutOverviewPage.getOrderItemCount();
        LOG.info("Order overview shows {} items", orderItems);
        Assert.assertEquals(orderItems, 2, "Overview should show 2 items");

        checkoutOverviewPage.tapFinish();

        // ========== STEP 7: Validate order completion ==========
        LOG.info("Step 7: Validating order completion");
        Assert.assertTrue(checkoutCompletePage.isPageLoaded(),
                "Checkout complete page should be displayed");
        Assert.assertTrue(checkoutCompletePage.isOrderCompletionValid(),
                "Order completion should show success message and Back Home button");

        // ========== STEP 8: Go back home and validate menu options ==========
        LOG.info("Step 8: Navigating back to home and validating menu options");
        checkoutCompletePage.tapBackHome();
        Assert.assertTrue(productsPage.isPageLoaded(),
                "Products page should be displayed after tapping Back Home");

        menuPage.openMenu();

        Assert.assertTrue(menuPage.isAllItemsDisplayed(),
                "'ALL ITEMS' menu option should be displayed");
        Assert.assertTrue(menuPage.isWebViewDisplayed(),
                "'WEBVIEW' menu option should be displayed");
        Assert.assertTrue(menuPage.isAboutDisplayed(),
                "'ABOUT' menu option should be displayed");
        Assert.assertTrue(menuPage.isLogoutDisplayed(),
                "'LOGOUT' menu option should be displayed");
        Assert.assertTrue(menuPage.isResetAppStateDisplayed(),
                "'RESET APP STATE' menu option should be displayed");

        LOG.info("All {} menu options verified: {}", MenuPage.EXPECTED_MENU_OPTIONS.size(),
                MenuPage.EXPECTED_MENU_OPTIONS);

        menuPage.closeMenu();
        LOG.info("E2E flow completed successfully");
    }
}
