package com.mediconnect.pages;

import com.mediconnect.base.BasePage;
import com.mediconnect.base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DoctorAppointmentsPage extends BasePage {
    public DoctorAppointmentsPage(WebDriver driver) {
        super(driver);
    }

    public DoctorAppointmentsPage open() {
        driver.get(DriverFactory.baseUrl() + "/appointments");
        return this;
    }

    public void acceptRequest(String patientEmail, String date, String problem) {
        WebElement row = waitForRequestRow(patientEmail, date, problem);
        WebElement acceptButton = row.findElement(By.cssSelector(".btn-accept"));
        click(acceptButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator(patientEmail, date, problem, "Accepted")));
    }

    public boolean isAccepted(String patientEmail, String date, String problem) {
        return !driver.findElements(rowLocator(patientEmail, date, problem, "Accepted")).isEmpty();
    }

    private WebElement waitForRequestRow(String patientEmail, String date, String problem) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator(patientEmail, date, problem, "Awaiting Approval")));
    }

    private By rowLocator(String patientEmail, String date, String problem, String status) {
        return By.xpath("//tr[contains(.,'" + patientEmail + "') and contains(.,'" + date + "') and contains(.,'"
                + problem + "') and contains(.,'" + status + "')]");
    }
}
