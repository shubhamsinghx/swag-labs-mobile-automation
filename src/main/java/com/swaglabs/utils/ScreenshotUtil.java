package com.swaglabs.utils;

import com.swaglabs.driver.DriverManager;

import io.qameta.allure.Allure;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtil captures and saves screenshots during test execution.
 * Integrates with Allure for automatic screenshot attachment on failures.
 */
public final class ScreenshotUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR = "reports/screenshots/";
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtil() {
        // Prevent instantiation
    }

    /**
     * Captures a screenshot and saves it to the reports directory.
     *
     * @param testName the name of the test for the screenshot filename
     * @return the absolute path of the saved screenshot, or null on failure
     */
    public static String captureScreenshot(String testName) {
        try {
            TakesScreenshot driver = (TakesScreenshot) DriverManager.getDriver();
            File source = driver.getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String fileName = sanitizeFileName(testName) + "_" + timestamp + ".png";
            File destination = new File(SCREENSHOT_DIR + fileName);

            FileUtils.copyFile(source, destination);
            LOG.info("Screenshot saved: {}", destination.getAbsolutePath());
            return destination.getAbsolutePath();
        } catch (IOException e) {
            LOG.error("Failed to save screenshot for test: {}", testName, e);
            return null;
        }
    }

    /**
     * Captures a screenshot and attaches it to the Allure report.
     *
     * @param testName the name of the test
     */
    public static void captureAndAttachToAllure(String testName) {
        try {
            byte[] screenshotBytes = ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(
                    testName + "_screenshot",
                    "image/png",
                    new ByteArrayInputStream(screenshotBytes),
                    ".png");

            LOG.info("Screenshot attached to Allure report for: {}", testName);
        } catch (Exception e) {
            LOG.error("Failed to attach screenshot to Allure for: {}", testName, e);
        }
    }

    /**
     * Captures a screenshot, saves it locally, and attaches it to Allure.
     *
     * @param testName the name of the test
     * @return the local file path of the screenshot
     */
    public static String captureAndSave(String testName) {
        captureAndAttachToAllure(testName);
        return captureScreenshot(testName);
    }

    /**
     * Sanitizes the test name for use as a filename.
     */
    private static String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9_\\-]", "_");
    }
}
