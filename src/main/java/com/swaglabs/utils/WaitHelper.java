package com.swaglabs.utils;

import com.swaglabs.config.ConfigReader;
import com.swaglabs.driver.DriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * WaitHelper provides explicit wait utilities for reliable element interactions.
 */
public final class WaitHelper {

    private static final int DEFAULT_TIMEOUT = ConfigReader.getInt("explicit.wait.seconds", 15);

    private WaitHelper() {
        // Prevent instantiation
    }

    /**
     * Waits until the element identified by the locator is visible.
     */
    public static WebElement waitForVisibility(By locator) {
        return waitForVisibility(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Waits until the element identified by the locator is visible within the given timeout.
     */
    public static WebElement waitForVisibility(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(
                DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the element is clickable.
     */
    public static WebElement waitForClickability(By locator) {
        return waitForClickability(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Waits until the element is clickable within the given timeout.
     */
    public static WebElement waitForClickability(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(
                DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits until at least one element matching the locator is present.
     */
    public static List<WebElement> waitForPresenceOfAll(By locator) {
        return waitForPresenceOfAll(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Waits until at least one element matching the locator is present within timeout.
     */
    public static List<WebElement> waitForPresenceOfAll(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(
                DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    /**
     * Waits until the element becomes invisible or is not present.
     */
    public static boolean waitForInvisibility(By locator) {
        return waitForInvisibility(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Waits until the element becomes invisible within the given timeout.
     */
    public static boolean waitForInvisibility(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(
                DriverManager.getDriver(), Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits for a given duration (use sparingly — prefer explicit waits).
     */
    public static void staticWait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
