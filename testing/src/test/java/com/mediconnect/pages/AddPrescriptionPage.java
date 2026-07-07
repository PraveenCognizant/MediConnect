package com.mediconnect.pages;

import com.mediconnect.base.BasePage;
import com.mediconnect.base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddPrescriptionPage extends BasePage {

    @FindBy(css = "form.rx-form")
    private WebElement prescriptionForm;

    @FindBy(name = "patientname")
    private WebElement patientSelect;

    @FindBy(name = "age")
    private WebElement ageInput;

    @FindBy(name = "gender")
    private WebElement genderSelect;

    @FindBy(name = "disease")
    private WebElement diseaseInput;

    @FindBy(name = "doctorname")
    private WebElement doctorNameInput;

    @FindBy(name = "prescription")
    private WebElement prescriptionNotes;

    @FindBy(css = "button.btn-submit-rx")
    private WebElement submitButton;

    @FindBy(css = ".rx-result-card")
    private WebElement resultCard;

    @FindBy(css = ".rx-result-title")
    private WebElement resultTitle;

    public AddPrescriptionPage(WebDriver driver) {
        super(driver);
    }

    public AddPrescriptionPage open() {
        driver.get(DriverFactory.baseUrl() + "/addprescription");
        return this;
    }

    public boolean isLoaded() {
        return isDisplayed(prescriptionForm) && isDisplayed(patientSelect);
    }

    public void selectPatientContaining(String patientNamePart) {
        // Use visible text matching to be robust.
        List<WebElement> options = patientSelect.findElements(By.tagName("option"));
        for (WebElement opt : options) {
            String label = opt.getText() == null ? "" : opt.getText().trim();
            String disabledAttr = opt.getAttribute("disabled");
            if (disabledAttr == null && opt.isEnabled() && label.contains(patientNamePart)) {
                opt.click();
                return;
            }
        }

        // fallback: try to select by visible text
        // (if there is an exact match, click/select it)
        for (WebElement opt : options) {
            String label = opt.getText() == null ? "" : opt.getText().trim();
            if (opt.isEnabled() && label.equalsIgnoreCase(patientNamePart)) {
                opt.click();
                return;
            }
        }
        throw new IllegalStateException("Could not find patient option containing: " + patientNamePart);
    }

    public void selectLastPatient() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 1. Wait until the options inside the dropdown are visible/populated
        wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(patientSelect, By.tagName("option")));

        // 2. Fetch all options
        List<WebElement> options = patientSelect.findElements(By.tagName("option"));

        // 3. Select the last option directly using JavaScript to trigger Angular change events
        if (!options.isEmpty()) {
            WebElement lastOption = options.get(options.size() - 1);

            org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", lastOption);
            js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", patientSelect);
        } else {
            throw new IllegalStateException("No patient options found in the dropdown");
        }
    }
    public String patientSelectedText() {
        WebElement selected = patientSelect.findElement(By.cssSelector("option:checked"));
        return selected.getText().trim();
    }

    public void fillRandomPrescriptionFields(String disease, String gender, String age, String notes) {
        selectByVisibleText(genderSelect, gender);
        type(diseaseInput, disease);
        type(ageInput, age);
        type(prescriptionNotes, notes);
    }

    public void ensureDoctorNamePresent() {
        wait.until(ExpectedConditions.visibilityOf(doctorNameInput));

        // Wait up to 10 seconds for Angular to bind the auto-filled value to the DOM
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> {
            String val = doctorNameInput.getAttribute("value");
            return val != null && !val.trim().isEmpty();
        });

        if (doctorNameInput.getAttribute("value").trim().isEmpty()) {
            throw new IllegalStateException("Doctor name is empty on Add Prescription page.");
        }
    }
    public void clickSubmit() {
        click(submitButton);
        wait.until(ExpectedConditions.visibilityOf(resultCard));
    }

    public String resultTitleText() {
        return textOf(resultTitle);
    }
}
