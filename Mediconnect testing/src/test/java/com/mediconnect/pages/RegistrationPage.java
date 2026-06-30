package com.mediconnect.pages;

import com.mediconnect.base.BasePage;
import com.mediconnect.base.DriverFactory;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegistrationPage extends BasePage {
    @FindBy(css = ".reg-form-title")
    private WebElement formTitle;

    @FindBy(xpath = "//button[contains(@class,'reg-tab') and contains(normalize-space(),'User')]")
    private WebElement userTab;

    @FindBy(xpath = "//button[contains(@class,'reg-tab') and contains(normalize-space(),'Doctor')]")
    private WebElement doctorTab;

    @FindBy(name = "username")
    private WebElement userNameInput;

    @FindBy(name = "useremail")
    private WebElement userEmailInput;

    @FindBy(name = "usergender")
    private WebElement userGenderSelect;

    @FindBy(name = "userage")
    private WebElement userAgeInput;

    @FindBy(name = "usermobile")
    private WebElement userMobileInput;

    @FindBy(name = "useraddress")
    private WebElement userAddressInput;

    @FindBy(css = "input[name='pwd']")
    private WebElement passwordInput;

    @FindBy(name = "ConfirmPassword")
    private WebElement confirmPasswordInput;

    @FindBy(css = ".reg-terms input[type='checkbox']")
    private WebElement termsCheckbox;

    @FindBy(css = ".reg-submit-btn")
    private WebElement createAccountButton;

    @FindBy(name = "doctorname")
    private WebElement doctorNameInput;

    @FindBy(name = "doctoremail")
    private WebElement doctorEmailInput;

    @FindBy(name = "doctorgender")
    private WebElement doctorGenderSelect;

    @FindBy(name = "doctormobile")
    private WebElement doctorMobileInput;

    @FindBy(name = "specialization")
    private WebElement specializationInput;

    @FindBy(name = "experience")
    private WebElement experienceInput;

    @FindBy(name = "previoushospital")
    private WebElement previousHospitalInput;

    @FindBy(name = "doctoraddress")
    private WebElement doctorAddressInput;

    @FindBy(css = ".strength-label")
    private WebElement passwordStrengthLabel;

    @FindBy(css = ".match-ok, .match-fail")
    private WebElement passwordMatchMessage;

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    public RegistrationPage open() {
        driver.get(DriverFactory.baseUrl() + "/registration");
        return this;
    }

    public boolean isLoaded() {
        return isDisplayed(formTitle) && isDisplayed(createAccountButton);
    }

    public String title() {
        return textOf(formTitle);
    }

    public void chooseUserTab() {
        click(userTab);
    }

    public void chooseDoctorTab() {
        click(doctorTab);
    }

    public void fillUserRegistration(String name, String email, String gender, String age,
                                     String mobile, String address, String password) {
        chooseUserTab();
        type(userNameInput, name);
        type(userEmailInput, email);
        selectByVisibleText(userGenderSelect, gender);
        type(userAgeInput, age);
        type(userMobileInput, mobile);
        type(userAddressInput, address);
        type(passwordInput, password);
        type(confirmPasswordInput, password);
    }

    public void fillDoctorRegistration(String name, String email, String gender, String mobile,
                                       String specialization, String experience,
                                       String hospital, String address, String password) {
        chooseDoctorTab();
        type(doctorNameInput, name);
        type(doctorEmailInput, email);
        selectByVisibleText(doctorGenderSelect, gender);
        type(doctorMobileInput, mobile);
        type(specializationInput, specialization);
        type(experienceInput, experience);
        type(previousHospitalInput, hospital);
        type(doctorAddressInput, address);
        type(passwordInput, password);
        type(confirmPasswordInput, password);
    }

    public void submitRegistration() {
        click(createAccountButton);
    }

    public boolean isOnPath(String path) {
        try {
            return wait.until(ExpectedConditions.urlContains(path));
        } catch (TimeoutException ex) {
            return false;
        }
    }

    public void acceptTerms() {
        if (!termsCheckbox.isSelected()) {
            click(termsCheckbox);
        }
    }

    public boolean isCreateAccountDisabled() {
        return !createAccountButton.isEnabled();
    }

    public String passwordStrength() {
        return textOf(passwordStrengthLabel);
    }

    public String passwordMatchMessage() {
        return textOf(passwordMatchMessage);
    }
}
