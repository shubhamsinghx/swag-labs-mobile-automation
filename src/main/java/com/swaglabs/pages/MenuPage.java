package com.swaglabs.pages;

import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;

import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

/**
 * MenuPage represents the Swag Labs side menu (hamburger menu).
 * Provides access to navigation options: All Items, WebView, About, Logout, Reset.
 */
public class MenuPage extends BasePage {

    // ==================== Locators ====================
    private static final By MENU_BUTTON = AppiumBy.accessibilityId("test-Menu");
    private static final By CLOSE_MENU_BUTTON = AppiumBy.accessibilityId("test-CLOSE MENU");
    private static final By ALL_ITEMS_OPTION = AppiumBy.accessibilityId("test-ALL ITEMS");
    private static final By WEBVIEW_OPTION = AppiumBy.accessibilityId("test-WEBVIEW");
    private static final By ABOUT_OPTION = AppiumBy.accessibilityId("test-ABOUT");
    private static final By LOGOUT_OPTION = AppiumBy.accessibilityId("test-LOGOUT");
    private static final By RESET_APP_OPTION = AppiumBy.accessibilityId("test-RESET APP STATE");

    /**
     * Expected menu options available in the side menu.
     */
    public static final List<String> EXPECTED_MENU_OPTIONS = Arrays.asList(
            "ALL ITEMS", "WEBVIEW", "ABOUT", "LOGOUT", "RESET APP STATE"
    );

    // ==================== Page Actions ====================

    @Step("Open side menu")
    public MenuPage openMenu() {
        tap(MENU_BUTTON);
        // Allow slide-in animation to finish
        try { Thread.sleep(1000); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
        return this;
    }

    @Step("Close side menu")
    public void closeMenu() {
        try {
            tap(CLOSE_MENU_BUTTON);
        } catch (Exception e) {
            // Fallback: Android back button reliably dismisses the side menu
            driver().navigate().back();
        }
    }

    @Step("Verify 'All Items' option is displayed")
    public boolean isAllItemsDisplayed() {
        return isDisplayed(ALL_ITEMS_OPTION);
    }

    @Step("Verify 'WebView' option is displayed")
    public boolean isWebViewDisplayed() {
        return isDisplayed(WEBVIEW_OPTION);
    }

    @Step("Verify 'About' option is displayed")
    public boolean isAboutDisplayed() {
        return isDisplayed(ABOUT_OPTION);
    }

    @Step("Verify 'Logout' option is displayed")
    public boolean isLogoutDisplayed() {
        return isDisplayed(LOGOUT_OPTION);
    }

    @Step("Verify 'Reset App State' option is displayed")
    public boolean isResetAppStateDisplayed() {
        return isDisplayed(RESET_APP_OPTION);
    }

    @Step("Validate all menu options are displayed")
    public boolean areAllMenuOptionsDisplayed() {
        return isAllItemsDisplayed()
                && isWebViewDisplayed()
                && isAboutDisplayed()
                && isLogoutDisplayed()
                && isResetAppStateDisplayed();
    }

    @Step("Tap 'All Items' option")
    public void tapAllItems() {
        tap(ALL_ITEMS_OPTION);
    }

    @Step("Tap 'WebView' option")
    public void tapWebView() {
        tap(WEBVIEW_OPTION);
    }

    @Step("Tap 'About' option")
    public void tapAbout() {
        tap(ABOUT_OPTION);
    }

    @Step("Tap 'Logout' option")
    public void tapLogout() {
        tap(LOGOUT_OPTION);
    }

    @Step("Tap 'Reset App State' option")
    public void tapResetAppState() {
        tap(RESET_APP_OPTION);
    }

    @Override
    public boolean isPageLoaded() {
        return isDisplayed(ALL_ITEMS_OPTION);
    }
}
