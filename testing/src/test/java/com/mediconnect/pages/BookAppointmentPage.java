package com.mediconnect.pages;

import com.mediconnect.base.BasePage;
import com.mediconnect.base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class BookAppointmentPage extends BasePage {
    @FindBy(css = ".appt-title")
    private WebElement pageTitle;

    @FindBy(name = "patientname")
    private WebElement patientNameInput;

    @FindBy(name = "patientemail")
    private WebElement patientEmailInput;

    @FindBy(name = "specialization")
    private WebElement specializationSelect;

    @FindBy(name = "doctor")
    private WebElement doctorSelect;

    @FindBy(name = "date")
    private WebElement dateSelect;

    @FindBy(name = "slot")
    private WebElement slotSelect;

    @FindBy(name = "problem")
    private WebElement problemInput;

    @FindBy(css = ".appt-submit-btn")
    private WebElement confirmBookingButton;

    @FindBy(css = ".appt-no-slots")
    private WebElement noSlotsMessage;

    @FindBy(css = ".appt-message, .appt-error-note")
    private WebElement appointmentMessage;

    public BookAppointmentPage(WebDriver driver) {
        super(driver);
    }

    public BookAppointmentPage open() {
        driver.get(DriverFactory.baseUrl() + "/bookappointment");
        return this;
    }

    public boolean isLoaded() {
        return isDisplayed(pageTitle) && textOf(pageTitle).contains("Book Appointment");
    }

    public String patientEmail() {
        return wait.until(ExpectedConditions.visibilityOf(patientEmailInput)).getAttribute("value");
    }

    public String patientName() {
        return wait.until(ExpectedConditions.visibilityOf(patientNameInput)).getAttribute("value");
    }

    public void chooseSpecializationContaining(String specializationText) {
        waitForOptions(specializationSelect);
        selectOptionContaining(specializationSelect, specializationText);
        waitForOptions(doctorSelect);
    }

    public void chooseDoctorContaining(String doctorName) {
        selectOptionContaining(doctorSelect, doctorName);
    }

    public boolean hasAvailableSlotsForSelectedDoctor() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("select[name='date'] option"), 1),
                    ExpectedConditions.visibilityOf(noSlotsMessage)
            ));
        } catch (TimeoutException ex) {
            return false;
        }
        return !isDisplayed(noSlotsMessage) && optionCount(dateSelect) > 1;
    }

    public void chooseFirstAvailableDate() {
        selectFirstRealOption(dateSelect);
        waitForOptions(slotSelect);
    }

    public void chooseDate(String date) {
        waitForOptions(dateSelect);
        selectOptionContaining(dateSelect, date);
        waitForOptions(slotSelect);
    }

    public void chooseFirstAvailableSlot() {
        selectFirstRealOption(slotSelect);
    }

    public void chooseSlot(String slot) {
        waitForOptions(slotSelect);
        selectOptionContaining(slotSelect, slot);
    }

    public void enterProblem(String problem) {
        type(problemInput, problem);
    }

    public void confirmBooking() {
        click(confirmBookingButton);
    }

    public boolean isConfirmBookingDisabled() {
        return !confirmBookingButton.isEnabled();
    }

    public boolean isOnUserDashboard() {
        try {
            return wait.until(ExpectedConditions.urlContains("/userdashboard"));
        } catch (TimeoutException ex) {
            return false;
        }
    }

    public String statusMessage() {
        return isDisplayed(appointmentMessage) ? textOf(appointmentMessage) : "";
    }

    private void waitForOptions(WebElement selectElement) {
        wait.until(driver -> optionCount(selectElement) > 1);
    }

    private int optionCount(WebElement selectElement) {
        return new Select(wait.until(ExpectedConditions.visibilityOf(selectElement))).getOptions().size();
    }

    private void selectFirstRealOption(WebElement selectElement) {
        Select select = new Select(wait.until(ExpectedConditions.elementToBeClickable(selectElement)));
        List<WebElement> options = select.getOptions();
        for (WebElement option : options) {
            String value = option.getAttribute("value");
            if (value != null && !value.trim().isEmpty() && option.isEnabled()) {
                select.selectByValue(value);
                return;
            }
        }
        throw new IllegalStateException("No selectable option found");
    }

    private void selectOptionContaining(WebElement selectElement, String text) {
        Select select = new Select(wait.until(ExpectedConditions.elementToBeClickable(selectElement)));
        String expected = text.toLowerCase();
        for (WebElement option : select.getOptions()) {
            String label = option.getText().trim();
            String value = option.getAttribute("value");
            if (option.isEnabled()
                    && (label.toLowerCase().contains(expected)
                    || (value != null && value.toLowerCase().contains(expected)))) {
                select.selectByVisibleText(label);
                return;
            }
        }
        throw new IllegalStateException("Could not find dropdown option containing: " + text);
    }
}
