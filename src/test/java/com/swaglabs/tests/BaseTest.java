package com.swaglabs.tests;

import com.swaglabs.driver.DriverManager;
import com.swaglabs.pages.*;
import com.swaglabs.utils.OrientationHelper;

import io.qameta.allure.Allure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

/**
 * BaseTest provides common setup and teardown logic for all test classes.
 * Handles driver initialization, orientation setting, and Allure environment info.
 *
 * TestNG parameter "orientation" (PORTRAIT/LANDSCAPE) controls the device orientation.
 */
@Listeners(com.swaglabs.listeners.TestListener.class)
public abstract class BaseTest {

    protected static final Logger LOG = LoggerFactory.getLogger(BaseTest.class);

    // Page objects — initialized after driver is ready
    protected LoginPage loginPage;
    protected ProductsPage productsPage;
    protected CartPage cartPage;
    protected CheckoutInfoPage checkoutInfoPage;
    protected CheckoutOverviewPage checkoutOverviewPage;
    protected CheckoutCompletePage checkoutCompletePage;
    protected MenuPage menuPage;

    // Test credentials
    protected static final String STANDARD_USER = "standard_user";
    protected static final String PASSWORD = "secret_sauce";

    @Parameters({"orientation"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("PORTRAIT") String orientation) {
        LOG.info("Setting up test — orientation: {}", orientation);

        // Initialize the Appium driver
        DriverManager.initDriver();

        // Set device orientation
        OrientationHelper.setOrientation(orientation);

        // Initialize page objects
        initializePageObjects();

        // Add environment info to Allure
        Allure.parameter("Orientation", orientation);
        Allure.parameter("Platform", "Android");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        LOG.info("Tearing down test — quitting driver");
        DriverManager.quitDriver();
    }

    /**
     * Initializes all page object instances.
     */
    private void initializePageObjects() {
        loginPage = new LoginPage();
        productsPage = new ProductsPage();
        cartPage = new CartPage();
        checkoutInfoPage = new CheckoutInfoPage();
        checkoutOverviewPage = new CheckoutOverviewPage();
        checkoutCompletePage = new CheckoutCompletePage();
        menuPage = new MenuPage();
    }

    /**
     * Performs login with the standard user credentials.
     * Can be called by any test that needs to start from a logged-in state.
     */
    protected void loginAsStandardUser() {
        loginPage.login(STANDARD_USER, PASSWORD);
    }
}
