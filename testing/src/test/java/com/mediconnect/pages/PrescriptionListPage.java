package com.mediconnect.pages;

import com.mediconnect.base.BasePage;
import com.mediconnect.base.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PrescriptionListPage extends BasePage {

    @FindBy(css = ".rx-form-layout, .section-card, body")
    private WebElement pageMarker;

    public PrescriptionListPage(WebDriver driver) {
        super(driver);
    }

    public PrescriptionListPage open() {
        driver.get(DriverFactory.baseUrl() + "/prescriptionlist");
        return this;
    }

    public void waitForLoaded() {
        wait.until(d -> pageMarker != null);
    }

    public boolean pageContainsText(String text) {
        return driver.getPageSource() != null && driver.getPageSource().contains(text);
    }

    public boolean hasPrescriptionForPatient(String patientName) {
        return pageContainsText(patientName);
    }
}
