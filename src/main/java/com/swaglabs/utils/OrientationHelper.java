package com.swaglabs.utils;

import com.swaglabs.driver.DriverManager;

import org.openqa.selenium.ScreenOrientation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OrientationHelper manages device orientation (portrait/landscape) during test execution.
 * Used to fulfill the requirement of running tests in both orientations.
 */
public final class OrientationHelper {

    private static final Logger LOG = LoggerFactory.getLogger(OrientationHelper.class);

    private OrientationHelper() {
        // Prevent instantiation
    }

    /**
     * Sets the device orientation to portrait.
     */
    public static void setPortrait() {
        setOrientation(ScreenOrientation.PORTRAIT);
    }

    /**
     * Sets the device orientation to landscape.
     */
    public static void setLandscape() {
        setOrientation(ScreenOrientation.LANDSCAPE);
    }

    /**
     * Sets the device orientation based on the given string value.
     *
     * @param orientation "PORTRAIT" or "LANDSCAPE" (case-insensitive)
     */
    public static void setOrientation(String orientation) {
        if (orientation == null || orientation.isBlank()) {
            LOG.warn("Orientation value is null/blank, defaulting to PORTRAIT");
            setPortrait();
            return;
        }
        ScreenOrientation screenOrientation = ScreenOrientation.valueOf(orientation.toUpperCase());
        setOrientation(screenOrientation);
    }

    /**
     * Sets the device orientation.
     */
    public static void setOrientation(ScreenOrientation orientation) {
        try {
            DriverManager.getDriver().rotate(orientation);
            LOG.info("Device orientation set to: {}", orientation);
        } catch (Exception e) {
            LOG.error("Failed to set orientation to {}: {}", orientation, e.getMessage());
            throw e;
        }
    }

    /**
     * Returns the current device orientation.
     */
    public static ScreenOrientation getOrientation() {
        return DriverManager.getDriver().getOrientation();
    }

    /**
     * Checks if the device is currently in portrait mode.
     */
    public static boolean isPortrait() {
        return getOrientation() == ScreenOrientation.PORTRAIT;
    }

    /**
     * Checks if the device is currently in landscape mode.
     */
    public static boolean isLandscape() {
        return getOrientation() == ScreenOrientation.LANDSCAPE;
    }
}
