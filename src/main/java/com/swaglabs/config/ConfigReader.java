package com.swaglabs.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConfigReader loads and provides access to configuration properties.
 * Supports multiple config files for local, LambdaTest, and BrowserStack execution.
 */
public final class ConfigReader {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties PROPERTIES = new Properties();
    private static final String DEFAULT_CONFIG = "config.properties";

    private ConfigReader() {
        // Prevent instantiation
    }

    static {
        loadConfig(resolveConfigFile());
    }

    /**
     * Resolves the config file based on the 'execution.platform' system property.
     * Supported values: lambdatest, browserstack. Falls back to default config.
     */
    private static String resolveConfigFile() {
        String platform = System.getProperty("execution.platform", "local");
        return switch (platform.toLowerCase()) {
            case "lambdatest" -> "config-lambdatest.properties";
            case "browserstack" -> "config-browserstack.properties";
            default -> DEFAULT_CONFIG;
        };
    }

    private static void loadConfig(String configFile) {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                LOG.warn("Config file '{}' not found, falling back to default", configFile);
                try (InputStream defaultInput = ConfigReader.class.getClassLoader()
                        .getResourceAsStream(DEFAULT_CONFIG)) {
                    if (defaultInput != null) {
                        PROPERTIES.load(defaultInput);
                    }
                }
                return;
            }
            PROPERTIES.load(input);
            LOG.info("Loaded configuration from: {}", configFile);
        } catch (IOException e) {
            LOG.error("Failed to load config file: {}", configFile, e);
            throw new RuntimeException("Unable to load configuration: " + configFile, e);
        }
    }

    /**
     * Returns the property value for the given key.
     * System properties take precedence over file-based properties.
     */
    public static String get(String key) {
        String systemProp = System.getProperty(key);
        return systemProp != null ? systemProp : PROPERTIES.getProperty(key);
    }

    /**
     * Returns the property value or a default if the key is not found.
     */
    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Returns the property value as an integer.
     */
    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            LOG.warn("Invalid integer for key '{}': {}", key, value);
            return defaultValue;
        }
    }

    /**
     * Returns the property value as a boolean.
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return value != null ? Boolean.parseBoolean(value.trim()) : defaultValue;
    }
}
