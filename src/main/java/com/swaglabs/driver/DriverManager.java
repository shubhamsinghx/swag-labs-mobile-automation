package com.swaglabs.driver;

import com.swaglabs.config.ConfigReader;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import org.openqa.selenium.MutableCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * DriverManager handles Appium AndroidDriver lifecycle using ThreadLocal
 * for thread-safe parallel test execution.
 *
 * Supports three execution platforms:
 * - local: Appium server running locally
 * - lambdatest: LambdaTest cloud platform
 * - browserstack: BrowserStack cloud platform
 */
public final class DriverManager {

    private static final Logger LOG = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<AndroidDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {
        // Prevent instantiation
    }

    /**
     * Returns the AndroidDriver instance for the current thread.
     */
    public static AndroidDriver getDriver() {
        AndroidDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            throw new IllegalStateException("Driver has not been initialized. Call initDriver() first.");
        }
        return driver;
    }

    /**
     * Initializes the AndroidDriver based on the configured execution platform.
     */
    public static void initDriver() {
        String platform = ConfigReader.get("execution.platform", "local");
        LOG.info("Initializing driver for platform: {}", platform);

        AndroidDriver driver = switch (platform.toLowerCase()) {
            case "lambdatest" -> createLambdaTestDriver();
            case "browserstack" -> createBrowserStackDriver();
            default -> createLocalDriver();
        };

        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getInt("implicit.wait.seconds", 10)));
        DRIVER_THREAD_LOCAL.set(driver);
        LOG.info("Driver initialized successfully for platform: {}", platform);
    }

    /**
     * Creates a local Appium AndroidDriver.
     */
    private static AndroidDriver createLocalDriver() {
        UiAutomator2Options options = new UiAutomator2Options();

        String appPath = ConfigReader.get("app.path");
        if (appPath != null && !appPath.isEmpty()) {
            File appFile = new File(appPath);
            if (!appFile.isAbsolute()) {
                appFile = new File(System.getProperty("user.dir"), appPath);
            }
            options.setApp(appFile.getAbsolutePath());
        }

        options.setPlatformName(ConfigReader.get("platform.name", "Android"));
        options.setDeviceName(ConfigReader.get("device.name", "Android Emulator"));

        String platformVersion = ConfigReader.get("platform.version");
        if (platformVersion != null) {
            options.setPlatformVersion(platformVersion);
        }

        options.setAutomationName(ConfigReader.get("automation.name", "UiAutomator2"));
        options.setAppPackage(ConfigReader.get("app.package", "com.swaglabsmobileapp"));
        options.setAppActivity(ConfigReader.get("app.activity", "com.swaglabsmobileapp.MainActivity"));
        options.setNoReset(ConfigReader.getBoolean("no.reset", false));
        options.setFullReset(ConfigReader.getBoolean("full.reset", false));
        options.setNewCommandTimeout(Duration.ofSeconds(ConfigReader.getInt("new.command.timeout", 300)));

        String appiumUrl = ConfigReader.get("appium.server.url", "http://127.0.0.1:4723");
        try {
            return new AndroidDriver(new URL(appiumUrl), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL: " + appiumUrl, e);
        }
    }

    /**
     * Creates a LambdaTest cloud AndroidDriver.
     */
    private static AndroidDriver createLambdaTestDriver() {
        UiAutomator2Options options = new UiAutomator2Options();

        options.setPlatformName(ConfigReader.get("platform.name", "Android"));
        options.setDeviceName(ConfigReader.get("device.name"));
        options.setPlatformVersion(ConfigReader.get("platform.version"));

        String appUrl = ConfigReader.get("app.url");
        if (appUrl != null) {
            options.setApp(appUrl);
        }

        Map<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("build", ConfigReader.get("lt.build", "SwagLabs-Automation"));
        ltOptions.put("name", ConfigReader.get("lt.test.name", "SwagLabs-Test"));
        ltOptions.put("isRealMobile", ConfigReader.getBoolean("lt.real.mobile", true));
        ltOptions.put("visual", ConfigReader.getBoolean("lt.visual", true));
        ltOptions.put("video", ConfigReader.getBoolean("lt.video", true));
        ltOptions.put("console", ConfigReader.getBoolean("lt.console", true));
        ltOptions.put("network", ConfigReader.getBoolean("lt.network", true));

        options.setCapability("lt:options", ltOptions);

        String username = ConfigReader.get("lt.username");
        String accessKey = ConfigReader.get("lt.access.key");
        String hubUrl = String.format("https://%s:%s@mobile-hub.lambdatest.com/wd/hub", username, accessKey);

        try {
            return new AndroidDriver(new URL(hubUrl), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid LambdaTest hub URL", e);
        }
    }

    /**
     * Creates a BrowserStack cloud AndroidDriver.
     */
    private static AndroidDriver createBrowserStackDriver() {
        MutableCapabilities capabilities = new MutableCapabilities();

        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("userName", ConfigReader.get("bs.username"));
        bstackOptions.put("accessKey", ConfigReader.get("bs.access.key"));
        bstackOptions.put("appiumVersion", ConfigReader.get("bs.appium.version", "2.0.0"));
        bstackOptions.put("projectName", ConfigReader.get("bs.project", "SwagLabs-Automation"));
        bstackOptions.put("buildName", ConfigReader.get("bs.build", "SwagLabs-Build"));
        bstackOptions.put("sessionName", ConfigReader.get("bs.session", "SwagLabs-Test"));
        bstackOptions.put("debug", ConfigReader.getBoolean("bs.debug", true));
        bstackOptions.put("networkLogs", ConfigReader.getBoolean("bs.network.logs", true));
        bstackOptions.put("video", ConfigReader.getBoolean("bs.video", true));

        capabilities.setCapability("bstack:options", bstackOptions);
        capabilities.setCapability("platformName", ConfigReader.get("platform.name", "Android"));
        capabilities.setCapability("appium:deviceName", ConfigReader.get("device.name"));
        capabilities.setCapability("appium:platformVersion", ConfigReader.get("platform.version"));
        capabilities.setCapability("appium:app", ConfigReader.get("app.url"));
        capabilities.setCapability("appium:automationName", "UiAutomator2");

        String hubUrl = "https://hub-cloud.browserstack.com/wd/hub";
        try {
            return new AndroidDriver(new URL(hubUrl), capabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid BrowserStack hub URL", e);
        }
    }

    /**
     * Quits the AndroidDriver and removes it from ThreadLocal.
     */
    public static void quitDriver() {
        AndroidDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                driver.quit();
                LOG.info("Driver quit successfully");
            } catch (Exception e) {
                LOG.warn("Error quitting driver: {}", e.getMessage());
            } finally {
                DRIVER_THREAD_LOCAL.remove();
            }
        }
    }
}
