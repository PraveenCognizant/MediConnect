package com.mediconnect.pages;

import com.mediconnect.base.BasePage;
import com.mediconnect.base.DriverFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    @FindBy(css = ".form-title")
    private WebElement formTitle;

    @FindBy(xpath = "//button[contains(@class,'role-tab') and contains(normalize-space(),'Patient')]")
    private WebElement patientTab;

    @FindBy(xpath = "//button[contains(@class,'role-tab') and contains(normalize-space(),'Doctor')]")
    private WebElement doctorTab;

    @FindBy(xpath = "//button[contains(@class,'role-tab') and contains(normalize-space(),'Admin')]")
    private WebElement adminTab;

    @FindBy(css = "div[ng-reflect-ng-if='true'] input[name='email'], input[name='email']")
    private WebElement emailInput;

    @FindBy(name = "doctoremail")
    private WebElement doctorEmailInput;

    @FindBy(name = "pwd")
    private WebElement passwordInput;

    @FindBy(css = ".btn-login")
    private WebElement signInButton;

    @FindBy(css = ".err-msg")
    private WebElement errorMessage;

    @FindBy(css = ".field-error")
    private WebElement fieldError;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(DriverFactory.baseUrl() + "/login");
        return this;
    }

    public LoginPage openLoggedOut() {
        driver.get(DriverFactory.baseUrl());
        ((JavascriptExecutor) driver).executeScript("localStorage.clear(); sessionStorage.clear();");
        driver.get(DriverFactory.baseUrl() + "/login");
        return this;
    }

    public String title() {
        return textOf(formTitle);
    }

    public boolean isLoaded() {
        return isDisplayed(formTitle) && isDisplayed(signInButton);
    }

    public void choosePatientTab() {
        click(patientTab);
    }

    public void chooseDoctorTab() {
        click(doctorTab);
    }

    public void chooseAdminTab() {
        click(adminTab);
    }

    public void loginAsPatient(String email, String password) {
        choosePatientTab();
        type(emailInput, email);
        type(passwordInput, password);
        click(signInButton);
    }

    public void loginAsDoctor(String email, String password) {
        chooseDoctorTab();
        type(doctorEmailInput, email);
        type(passwordInput, password);
        click(signInButton);
    }

    public void loginAsAdmin(String email, String password) {
        chooseAdminTab();
        type(emailInput, email);
        type(passwordInput, password);
        click(signInButton);
    }

    public void loginAsRole(String role, String email, String password) {
        if ("doctor".equalsIgnoreCase(role)) {
            loginAsDoctor(email, password);
        } else if ("admin".equalsIgnoreCase(role)) {
            loginAsAdmin(email, password);
        } else {
            loginAsPatient(email, password);
        }
    }

    public void enterPatientEmail(String email) {
        choosePatientTab();
        type(emailInput, email);
    }

    public void enterEmailForRole(String role, String email) {
        if ("doctor".equalsIgnoreCase(role)) {
            chooseDoctorTab();
            type(doctorEmailInput, email);
        } else if ("admin".equalsIgnoreCase(role)) {
            chooseAdminTab();
            type(emailInput, email);
        } else {
            enterPatientEmail(email);
        }
    }

    public void enterCredentialsForRoleWithoutSubmitting(String role, String email, String password) {
        if ("doctor".equalsIgnoreCase(role)) {
            chooseDoctorTab();
            type(doctorEmailInput, email);
        } else if ("admin".equalsIgnoreCase(role)) {
            chooseAdminTab();
            type(emailInput, email);
        } else {
            choosePatientTab();
            type(emailInput, email);
        }
        type(passwordInput, password);
    }

    public boolean isSignInDisabled() {
        return !signInButton.isEnabled();
    }

    public boolean isOnPath(String path) {
        try {
            return wait.until(ExpectedConditions.urlContains(path));
        } catch (TimeoutException ex) {
            return false;
        }
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    public String validationMessage() {
        return textOf(fieldError);
    }

    public String errorMessage() {
        return textOf(errorMessage);
    }
}
