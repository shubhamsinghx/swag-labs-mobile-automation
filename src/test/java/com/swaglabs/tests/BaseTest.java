package com.swaglabs.tests;

import com.swaglabs.config.ConfigReader;
import com.swaglabs.driver.DriverManager;
import com.swaglabs.pages.*;
import com.swaglabs.utils.OrientationHelper;

import io.qameta.allure.Allure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

/**
 * BaseTest provides common setup and teardown logic for all test classes.
 *
 * Lifecycle:
 *   @BeforeClass  — create driver once per test class
 *   @BeforeMethod — reset app to login screen (fast, no session restart)
 *   @AfterClass   — quit driver once per test class
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

    private static final String APP_PACKAGE = "com.swaglabsmobileapp";

    /**
     * Creates the Appium driver once per test class and sets orientation.
     */
    @Parameters({"orientation"})
    @BeforeClass(alwaysRun = true)
    public void setUpDriver(@Optional("PORTRAIT") String orientation) {
        LOG.info("Initializing driver — orientation: {}", orientation);
        DriverManager.initDriver();
        OrientationHelper.setOrientation(orientation);
        initializePageObjects();
        Allure.parameter("Orientation", orientation);
        Allure.parameter("Platform", "Android");
    }

    /**
     * Resets the app to the login screen before each test method.
     * Much faster than creating a new driver session.
     * Subclasses can override to also perform login after reset.
     */
    @BeforeMethod(alwaysRun = true)
    public void resetApp() {
        LOG.info("Resetting app to start screen");
        DriverManager.getDriver().terminateApp(APP_PACKAGE);
        DriverManager.getDriver().activateApp(APP_PACKAGE);
    }

    /**
     * Quits the driver once after all tests in the class have run.
     */
    @AfterClass(alwaysRun = true)
    public void tearDown() {
        LOG.info("Tearing down — quitting driver");
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
     */
    protected void loginAsStandardUser() {
        loginPage.login(STANDARD_USER, PASSWORD);
    }
}
