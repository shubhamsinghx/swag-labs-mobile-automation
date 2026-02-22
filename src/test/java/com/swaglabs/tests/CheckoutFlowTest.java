package com.swaglabs.tests;

import com.swaglabs.listeners.RetryAnalyzer;

import io.qameta.allure.*;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * CheckoutFlowTest validates the complete checkout flow on the Swag Labs app.
 *
 * Test Scenarios:
 * - Complete checkout by providing address details
 * - Verify checkout overview with payment and shipping info
 * - Complete order and validate order completion details
 * - Verify navigation back to home page after order completion
 */
@Epic("Swag Labs Mobile Automation")
@Feature("Checkout Flow")
public class CheckoutFlowTest extends BaseTest {

    // Test data for checkout
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String ZIP_CODE = "10001";

    @Override
    @BeforeMethod(alwaysRun = true)
    public void setUp(String orientation) {
        super.setUp(orientation);
        loginAsStandardUser();
    }

    /**
     * Helper: Adds products and navigates to checkout info page.
     */
    private void navigateToCheckoutInfo(int productCount) {
        productsPage.addMultipleProductsToCart(productCount);
        productsPage.tapCartIcon();
        cartPage.tapCheckout();
    }

    @Test(description = "TC_CHECKOUT_001 — Fill checkout info and proceed to overview",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Checkout Information")
    @Description("Fill in first name, last name, and zip code, then proceed to checkout overview")
    public void testFillCheckoutInfoAndProceed() {
        LOG.info("Filling checkout info and proceeding to overview");

        navigateToCheckoutInfo(1);

        Assert.assertTrue(checkoutInfoPage.isPageLoaded(),
                "Checkout info page should be displayed");

        checkoutInfoPage.fillCheckoutInfo(FIRST_NAME, LAST_NAME, ZIP_CODE);
        checkoutInfoPage.tapContinue();

        Assert.assertTrue(checkoutOverviewPage.isPageLoaded(),
                "Checkout overview page should be displayed after filling info");
    }

    @Test(description = "TC_CHECKOUT_002 — Verify checkout overview displays order details",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Checkout Overview")
    @Description("Verify checkout overview displays items, payment info, and total")
    public void testCheckoutOverviewDisplaysOrderDetails() {
        LOG.info("Verifying checkout overview order details");

        navigateToCheckoutInfo(2);
        checkoutInfoPage.fillCheckoutInfo(FIRST_NAME, LAST_NAME, ZIP_CODE);
        checkoutInfoPage.tapContinue();

        Assert.assertTrue(checkoutOverviewPage.isPageLoaded(),
                "Checkout overview page should be loaded");

        // Verify order items
        int orderItemCount = checkoutOverviewPage.getOrderItemCount();
        LOG.info("Order item count on overview: {}", orderItemCount);
        Assert.assertEquals(orderItemCount, 2,
                "Checkout overview should show 2 items");

        // Verify total price is displayed
        String totalPrice = checkoutOverviewPage.getTotalPrice();
        LOG.info("Total price on overview: {}", totalPrice);
        Assert.assertNotNull(totalPrice, "Total price should be displayed");
        Assert.assertFalse(totalPrice.isEmpty(), "Total price should not be empty");
    }

    @Test(description = "TC_CHECKOUT_003 — Complete E2E checkout and validate order completion",
            retryAnalyzer = RetryAnalyzer.class, priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Story("Order Completion")
    @Description("Complete the full checkout flow: add products, fill info, finish order, and validate completion")
    public void testCompleteCheckoutAndValidateOrder() {
        LOG.info("Executing complete E2E checkout flow");

        // Step 1: Add multiple products to cart
        productsPage.addMultipleProductsToCart(2);
        LOG.info("Added 2 products to cart");

        // Step 2: Navigate to cart and proceed to checkout
        productsPage.tapCartIcon();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should be loaded");
        cartPage.tapCheckout();

        // Step 3: Fill checkout information (address and payment details)
        Assert.assertTrue(checkoutInfoPage.isPageLoaded(), "Checkout info page should be loaded");
        checkoutInfoPage.fillCheckoutInfo(FIRST_NAME, LAST_NAME, ZIP_CODE);
        checkoutInfoPage.tapContinue();
        LOG.info("Checkout info filled — First: {}, Last: {}, Zip: {}", FIRST_NAME, LAST_NAME, ZIP_CODE);

        // Step 4: Review and finish the order
        Assert.assertTrue(checkoutOverviewPage.isPageLoaded(), "Checkout overview should be loaded");
        checkoutOverviewPage.tapFinish();
        LOG.info("Order finished — verifying completion");

        // Step 5: Validate order completion
        Assert.assertTrue(checkoutCompletePage.isPageLoaded(),
                "Checkout complete page should be displayed");
        Assert.assertTrue(checkoutCompletePage.isOrderCompletionValid(),
                "Order completion elements should be visible (header + back home button)");

        LOG.info("Order completion validated successfully");
    }

    @Test(description = "TC_CHECKOUT_004 — Navigate back to home after order completion",
            retryAnalyzer = RetryAnalyzer.class, priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Post-Order Navigation")
    @Description("Complete checkout, then tap Back Home and verify return to Products page")
    public void testNavigateBackToHomeAfterOrder() {
        LOG.info("Completing checkout and navigating back to home");

        // Complete the full checkout flow
        navigateToCheckoutInfo(1);
        checkoutInfoPage.fillCheckoutInfo(FIRST_NAME, LAST_NAME, ZIP_CODE);
        checkoutInfoPage.tapContinue();
        checkoutOverviewPage.tapFinish();

        Assert.assertTrue(checkoutCompletePage.isPageLoaded(),
                "Checkout complete page should be displayed");

        // Navigate back to home
        checkoutCompletePage.tapBackHome();

        Assert.assertTrue(productsPage.isPageLoaded(),
                "Products page should be displayed after tapping Back Home");
        LOG.info("Successfully navigated back to Products page");
    }

    @Test(description = "TC_CHECKOUT_005 — Checkout with empty info shows error",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("Checkout Validation")
    @Description("Attempt to proceed with empty checkout info and verify error message")
    public void testCheckoutWithEmptyInfoShowsError() {
        LOG.info("Testing checkout with empty info");

        navigateToCheckoutInfo(1);
        checkoutInfoPage.tapContinue();

        Assert.assertTrue(checkoutInfoPage.isErrorDisplayed(),
                "Error message should be displayed when checkout info is empty");
    }
}
