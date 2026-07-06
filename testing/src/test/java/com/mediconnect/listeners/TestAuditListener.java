package com.mediconnect.listeners;

import com.mediconnect.base.DriverFactory;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestAuditListener implements ITestListener {
    private static final String AUDIT_LOG_FILE = "audit.log";

    private void logEvent(String event) {
        try (FileWriter fw = new FileWriter(AUDIT_LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pw.println("[" + timestamp + "] " + event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(ITestContext context) {
        logEvent("TEST SUITE STARTED: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logEvent("TEST SUITE FINISHED: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        logEvent("TEST STARTED: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logEvent("TEST PASSED: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logEvent("TEST FAILED: " + result.getName() + " - Reason: " + result.getThrowable());
        captureScreenshot(result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logEvent("TEST SKIPPED: " + result.getName());
    }

    @Attachment(value = "Failure Screenshot - {testName}", type = "image/png")
    private byte[] captureScreenshot(String testName) {
        try {
            WebDriver driver = DriverFactory.getDriver();
            if (driver != null && driver instanceof TakesScreenshot) {
                return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            }
        } catch (Exception e) {
            logEvent("Failed to capture screenshot: " + e.getMessage());
        }
        return new byte[0];
    }
}
