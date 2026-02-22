package com.swaglabs.listeners;

import com.swaglabs.config.ConfigReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer automatically retries failed tests up to the configured maximum.
 * Helps identify flaky tests and provides retry counts in the report.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOG = LoggerFactory.getLogger(RetryAnalyzer.class);
    private static final int MAX_RETRY_COUNT = ConfigReader.getInt("max.retry.count", 2);

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            LOG.warn("Retrying test '{}' — attempt {}/{}",
                    result.getName(), retryCount, MAX_RETRY_COUNT);
            return true;
        }
        return false;
    }

    /**
     * Returns the current retry count (useful for reporting).
     */
    public int getRetryCount() {
        return retryCount;
    }
}
