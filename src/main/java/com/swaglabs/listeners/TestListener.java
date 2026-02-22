package com.swaglabs.listeners;

import com.swaglabs.utils.ScreenshotUtil;

import io.qameta.allure.Allure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestListener implements ITestListener to handle test lifecycle events.
 * Automatically captures screenshots on failure and logs test execution details.
 */
public class TestListener implements ITestListener {

    private static final Logger LOG = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        LOG.info("========== Test Suite Started: {} ==========", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        LOG.info("========== Test Suite Finished: {} ==========", context.getName());
        LOG.info("Passed: {} | Failed: {} | Skipped: {}",
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("▶ Starting test: {}.{}",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("✓ PASSED: {}.{} [{}ms]",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName(),
                result.getEndMillis() - result.getStartMillis());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("✗ FAILED: {}.{} — {}",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName(),
                result.getThrowable().getMessage());

        // Capture screenshot on failure
        String screenshotPath = ScreenshotUtil.captureAndSave(
                result.getTestClass().getRealClass().getSimpleName() + "_" +
                        result.getMethod().getMethodName());

        if (screenshotPath != null) {
            Allure.addAttachment("Failure Screenshot Path", screenshotPath);
        }

        // Attach exception details to Allure
        Allure.addAttachment("Exception",
                result.getThrowable().toString());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("⊘ SKIPPED: {}.{}",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());

        if (result.getThrowable() != null) {
            LOG.warn("Skip reason: {}", result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        LOG.warn("⚠ Test failed but within success percentage: {}.{}",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
    }
}
