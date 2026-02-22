package com.swaglabs.pages;

import com.swaglabs.driver.DriverManager;
import com.swaglabs.utils.WaitHelper;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * BasePage provides common interaction methods shared across all page objects.
 * All page classes should extend this class to inherit reusable mobile actions.
 */
public abstract class BasePage {

    /**
     * Returns the AndroidDriver instance.
     */
    protected AndroidDriver driver() {
        return DriverManager.getDriver();
    }

    // ==================== Element Interaction Methods ====================

    /**
     * Taps on the element identified by the locator after waiting for it to be clickable.
     */
    protected void tap(By locator) {
        WaitHelper.waitForClickability(locator).click();
    }

    /**
     * Types text into the element identified by the locator.
     * Clears existing text before typing.
     */
    protected void type(By locator, String text) {
        WebElement element = WaitHelper.waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Returns the visible text of the element.
     */
    protected String getText(By locator) {
        return WaitHelper.waitForVisibility(locator).getText();
    }

    /**
     * Returns the text from the element's content-desc attribute (accessibility ID).
     */
    protected String getContentDesc(By locator) {
        return WaitHelper.waitForVisibility(locator).getAttribute("content-desc");
    }

    /**
     * Checks if an element is displayed on the screen.
     */
    protected boolean isDisplayed(By locator) {
        try {
            return WaitHelper.waitForVisibility(locator, 5).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Finds an element by accessibility ID.
     */
    protected WebElement findByAccessibilityId(String id) {
        return WaitHelper.waitForVisibility(AppiumBy.accessibilityId(id));
    }

    /**
     * Taps on an element by accessibility ID.
     */
    protected void tapByAccessibilityId(String id) {
        tap(AppiumBy.accessibilityId(id));
    }

    /**
     * Types into an element by accessibility ID.
     */
    protected void typeByAccessibilityId(String id, String text) {
        type(AppiumBy.accessibilityId(id), text);
    }

    /**
     * Finds all elements matching the locator.
     */
    protected List<WebElement> findAll(By locator) {
        return WaitHelper.waitForPresenceOfAll(locator);
    }

    // ==================== Scroll & Swipe Methods ====================

    /**
     * Scrolls down the screen by performing a swipe gesture.
     */
    protected void scrollDown() {
        Dimension size = driver().manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.7);
        int endY = (int) (size.height * 0.3);
        swipe(startX, startY, startX, endY);
    }

    /**
     * Scrolls up the screen by performing a swipe gesture.
     */
    protected void scrollUp() {
        Dimension size = driver().manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.3);
        int endY = (int) (size.height * 0.7);
        swipe(startX, startY, startX, endY);
    }

    /**
     * Performs a swipe from one point to another using W3C Actions.
     */
    protected void swipe(int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipeAction = new Sequence(finger, 1);

        swipeAction.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipeAction.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipeAction.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), endX, endY));
        swipeAction.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver().perform(Collections.singletonList(swipeAction));
    }

    /**
     * Scrolls down until the element with the given text is visible (UiScrollable).
     */
    protected WebElement scrollToText(String text) {
        return driver().findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().text(\"" + text + "\"))"));
    }

    /**
     * Scrolls down until an element with the given description is visible.
     */
    protected WebElement scrollToDescription(String description) {
        return driver().findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().description(\"" + description + "\"))"));
    }

    // ==================== Utility Methods ====================

    /**
     * Waits for the page to be fully loaded. Override in subclasses to define
     * page-specific load conditions.
     */
    public abstract boolean isPageLoaded();
}
