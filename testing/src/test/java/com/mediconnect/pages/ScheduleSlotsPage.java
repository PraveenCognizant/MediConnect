package com.mediconnect.pages;

import com.mediconnect.base.BasePage;
import com.mediconnect.base.DriverFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class ScheduleSlotsPage extends BasePage {
    @FindBy(css = ".btn-add-schedule")
    private WebElement addNewScheduleButton;

    @FindBy(css = ".date-input")
    private WebElement dateInput;

    @FindBy(name = "amAvailable")
    private WebElement morningCheckbox;

    @FindBy(name = "noonAvailable")
    private WebElement afternoonCheckbox;

    @FindBy(name = "pmAvailable")
    private WebElement eveningCheckbox;

    @FindBy(css = ".btn-save")
    private WebElement saveScheduleButton;

    public ScheduleSlotsPage(WebDriver driver) {
        super(driver);
    }

    public ScheduleSlotsPage open() {
        driver.get(DriverFactory.baseUrl() + "/scheduleslots");
        return this;
    }

    public void addSchedule(String date, String session) {
        click(addNewScheduleButton);
        setInputValue(dateInput, date);
        chooseSession(session);
        wait.until(ExpectedConditions.elementToBeClickable(saveScheduleButton));
        click(saveScheduleButton);
        waitForScheduleToAppear(date);
    }

    public boolean hasSchedule(String date) {
        return !driver.findElements(By.xpath("//div[contains(@class,'slot-card') and contains(.,'" + date + "')]")).isEmpty();
    }

    private void chooseSession(String session) {
        String normalized = session.toLowerCase();
        if (normalized.contains("am") || normalized.contains("morning")) {
            check(morningCheckbox);
        } else if (normalized.contains("noon") || normalized.contains("afternoon")) {
            check(afternoonCheckbox);
        } else if (normalized.contains("pm") || normalized.contains("evening")) {
            check(eveningCheckbox);
        } else {
            throw new IllegalArgumentException("Unsupported session: " + session);
        }
    }

    private void check(WebElement checkbox) {
        if (!checkbox.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        }
    }

    private void waitForScheduleToAppear(String date) {
        try {
            wait.withTimeout(Duration.ofSeconds(20))
                    .until(ExpectedConditions.visibilityOfElementLocated(scheduleCardFor(date)));
        } finally {
            wait.withTimeout(Duration.ofSeconds(10));
        }
    }

    private By scheduleCardFor(String date) {
        return By.xpath("//div[contains(@class,'slot-card') and contains(.,'" + date + "')]");
    }
}
