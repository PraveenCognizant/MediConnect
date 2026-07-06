package com.mediconnect.pages;

import com.mediconnect.base.BasePage;
import com.mediconnect.base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class AdminDoctorApprovalsPage extends BasePage {
    public AdminDoctorApprovalsPage(WebDriver driver) {
        super(driver);
    }

    public AdminDoctorApprovalsPage open() {
        driver.get(DriverFactory.baseUrl() + "/approvedoctors");
        return this;
    }

    public boolean hasPendingDoctor(String email) {
        return waitForDoctorRow(email) && !driver.findElements(pendingDoctorRow(email)).isEmpty();
    }

    public void approveDoctor(String email) {
        if (isDoctorAccepted(email)) {
            return;
        }
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(pendingDoctorRow(email)));
        click(row.findElement(By.cssSelector(".btn-accept")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(acceptedDoctorRow(email)));
    }

    public boolean isDoctorAccepted(String email) {
        return waitForDoctorRow(email) && !driver.findElements(acceptedDoctorRow(email)).isEmpty();
    }

    public boolean waitForDoctorRow(String email) {
        try {
            wait.withTimeout(Duration.ofSeconds(30)).until(driver -> {
                if (!driver.findElements(anyDoctorRow(email)).isEmpty()) {
                    return true;
                }
                driver.navigate().refresh();
                return !driver.findElements(anyDoctorRow(email)).isEmpty();
            });
            return true;
        } catch (TimeoutException ex) {
            return false;
        } finally {
            wait.withTimeout(Duration.ofSeconds(10));
        }
    }

    public String tableText() {
        return driver.findElement(By.cssSelector(".approv-table")).getText();
    }

    private By pendingDoctorRow(String email) {
        return By.xpath("//tr[contains(.,'" + email + "') and .//button[contains(@class,'btn-accept')]]");
    }

    private By acceptedDoctorRow(String email) {
        return By.xpath("//tr[contains(.,'" + email + "') and contains(.,'Accepted')]");
    }

    private By anyDoctorRow(String email) {
        return By.xpath("//tr[contains(.,'" + email + "')]");
    }
}
