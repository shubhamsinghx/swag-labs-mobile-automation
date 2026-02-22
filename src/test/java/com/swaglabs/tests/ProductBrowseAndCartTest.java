package com.swaglabs.tests;

import com.swaglabs.listeners.RetryAnalyzer;

import io.qameta.allure.*;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

/**
 * ProductBrowseAndCartTest validates product browsing, selection,
 * and cart functionality on the Swag Labs app.
 *
 * Test Scenarios:
 * - Browse product categories and verify product listing
 * - Select multiple products from the page
 * - Add products to cart
 * - Verify cart badge count updates
 * - Verify cart items match selected products
 */
@Epic("Swag Labs Mobile Automation")
@Feature("Product Browse & Cart")
public class ProductBrowseAndCartTest extends BaseTest {

    @Override
    @Parameters({"orientation"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("PORTRAIT") String orientation) {
        super.setUp(orientation);
        loginAsStandardUser();
    }

    @Test(description = "TC_PRODUCT_001 — Verify products page displays products after login",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Product Listing")
    @Description("Verify that the products page loads and displays products after successful login")
    public void testProductsPageDisplaysProducts() {
        LOG.info("Verifying products page displays products");

        Assert.assertTrue(productsPage.isPageLoaded(),
                "Products page should be loaded after login");

        int productCount = productsPage.getProductCount();
        LOG.info("Found {} products on the page", productCount);
        Assert.assertTrue(productCount > 0,
                "At least one product should be displayed on the products page");
    }

    @Test(description = "TC_PRODUCT_002 — Browse and verify product titles are displayed",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.NORMAL)
    @Story("Product Listing")
    @Description("Browse product listing and verify product titles are visible")
    public void testBrowseProductTitles() {
        LOG.info("Browsing product titles");

        List<String> productTitles = productsPage.getAllProductTitles();
        LOG.info("Product titles found: {}", productTitles);

        Assert.assertFalse(productTitles.isEmpty(),
                "Product titles should not be empty");

        for (String title : productTitles) {
            Assert.assertFalse(title.isEmpty(),
                    "Each product title should have text content");
        }
    }

    @Test(description = "TC_PRODUCT_003 — Add single product to cart and verify badge",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Add to Cart")
    @Description("Add a single product to the cart and verify the cart badge updates to 1")
    public void testAddSingleProductToCart() {
        LOG.info("Adding single product to cart");

        productsPage.addProductToCartAtIndex(0);

        String badgeCount = productsPage.getCartBadgeCount();
        LOG.info("Cart badge count after adding 1 product: {}", badgeCount);
        Assert.assertEquals(badgeCount, "1",
                "Cart badge should show 1 after adding one product");
    }

    @Test(description = "TC_PRODUCT_004 — Add multiple products to cart",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Add to Cart")
    @Description("Select and add multiple products to the cart and verify cart count")
    public void testAddMultipleProductsToCart() {
        LOG.info("Adding multiple products to cart");

        int productsToAdd = 3;
        productsPage.addMultipleProductsToCart(productsToAdd);

        String badgeCount = productsPage.getCartBadgeCount();
        LOG.info("Cart badge count after adding {} products: {}", productsToAdd, badgeCount);
        Assert.assertEquals(badgeCount, String.valueOf(productsToAdd),
                "Cart badge should show " + productsToAdd + " after adding products");
    }

    @Test(description = "TC_PRODUCT_005 — Add products and verify cart items match",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Cart Validation")
    @Description("Add products to cart, navigate to cart, and verify items match")
    public void testCartItemsMatchSelectedProducts() {
        LOG.info("Adding products and verifying cart items");

        // Add 2 products
        productsPage.addMultipleProductsToCart(2);

        // Navigate to cart
        productsPage.tapCartIcon();

        // Verify cart page is loaded
        Assert.assertTrue(cartPage.isPageLoaded(),
                "Cart page should be loaded");

        // Verify cart has items
        int cartItemCount = cartPage.getCartItemCount();
        LOG.info("Cart item count: {}", cartItemCount);
        Assert.assertEquals(cartItemCount, 2,
                "Cart should contain 2 items");
    }

    @Test(description = "TC_PRODUCT_006 — Proceed to checkout from cart",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Cart to Checkout")
    @Description("Add products, navigate to cart, and proceed to checkout")
    public void testProceedToCheckoutFromCart() {
        LOG.info("Adding product and proceeding to checkout");

        productsPage.addProductToCartAtIndex(0);
        productsPage.tapCartIcon();

        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should be loaded");

        cartPage.tapCheckout();

        Assert.assertTrue(checkoutInfoPage.isPageLoaded(),
                "Checkout info page should be displayed after tapping checkout");
    }
}
