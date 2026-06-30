package com.mediconnect.steps;

import com.mediconnect.base.DriverFactory;
import com.mediconnect.pages.LoginPage;
import com.mediconnect.pages.RegistrationPage;
import com.mediconnect.pages.WelcomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class UiStepDefinitions {
    private WelcomePage welcomePage;
    private LoginPage loginPage;
    private RegistrationPage registrationPage;

    @Given("I open the MediConnect welcome page")
    public void iOpenTheMediConnectWelcomePage() {
        welcomePage = new WelcomePage(DriverFactory.getDriver()).open();
    }

    @Then("the welcome page should show the MediConnect hero")
    public void theWelcomePageShouldShowTheMediConnectHero() {
        Assert.assertTrue(welcomePage.isLoaded(), "Welcome page hero was not visible");
    }

    @When("I navigate to login from the welcome page")
    public void iNavigateToLoginFromTheWelcomePage() {
        loginPage = welcomePage.goToLogin();
    }

    @Then("the patient login form should be displayed")
    public void thePatientLoginFormShouldBeDisplayed() {
        Assert.assertTrue(loginPage.isLoaded(), "Login page was not loaded");
        Assert.assertEquals(loginPage.title(), "Patient Login");
    }

    @When("I switch to the {string} login tab")
    public void iSwitchToTheLoginTab(String role) {
        loginPage = new LoginPage(DriverFactory.getDriver());
        if ("doctor".equalsIgnoreCase(role)) {
            loginPage.chooseDoctorTab();
        } else if ("admin".equalsIgnoreCase(role)) {
            loginPage.chooseAdminTab();
        } else {
            loginPage.choosePatientTab();
        }
    }

    @Then("the login form title should be {string}")
    public void theLoginFormTitleShouldBe(String expectedTitle) {
        Assert.assertEquals(loginPage.title(), expectedTitle);
    }

    @Given("I open the login page")
    public void iOpenTheLoginPage() {
        loginPage = new LoginPage(DriverFactory.getDriver()).open();
    }

    @When("I login as {string} with email {string} and password {string}")
    public void iLoginAsWithEmailAndPassword(String role, String email, String password) {
        loginPage.loginAsRole(role, email, password);
    }

    @When("I enter invalid email {string} for {string} login")
    public void iEnterInvalidEmailForLogin(String email, String role) {
        String password = "admin".equalsIgnoreCase(role)
                ? "Admin@123"
                : "doctor".equalsIgnoreCase(role) ? "Praveen@123" : "John@123";
        loginPage.enterCredentialsForRoleWithoutSubmitting(role, email, password);
    }

    @Then("I should be redirected to {string}")
    public void iShouldBeRedirectedTo(String expectedPath) {
        Assert.assertTrue(loginPage.isOnPath(expectedPath), "Expected URL path was not reached: " + expectedPath);
    }

    @Then("a login error containing {string} should be shown")
    public void aLoginErrorContainingShouldBeShown(String expectedMessage) {
        Assert.assertTrue(loginPage.errorMessage().contains(expectedMessage), "Expected login error was not shown");
    }

    @Then("the login submit button should be disabled")
    public void theLoginSubmitButtonShouldBeDisabled() {
        Assert.assertTrue(loginPage.isSignInDisabled(), "Login submit button should be disabled");
    }

    @Given("I open the registration page")
    public void iOpenTheRegistrationPage() {
        registrationPage = new RegistrationPage(DriverFactory.getDriver()).open();
    }

    @When("I fill valid patient registration details")
    public void iFillValidPatientRegistrationDetails() {
        registrationPage.fillUserRegistration(
                "Rahul Test",
                "rahul.test@example.com",
                "Male",
                "30",
                "9876543210",
                "Bangalore",
                "Strong@123"
        );
        registrationPage.acceptTerms();
    }

    @Then("the patient registration submit button should be enabled")
    public void thePatientRegistrationSubmitButtonShouldBeEnabled() {
        Assert.assertFalse(registrationPage.isCreateAccountDisabled(), "Patient registration submit button is disabled");
    }

    @When("I fill valid doctor registration details")
    public void iFillValidDoctorRegistrationDetails() {
        registrationPage.fillDoctorRegistration(
                "Dr Ananya Rao",
                "ananya.rao@example.com",
                "Female",
                "9876501234",
                "Cardiology",
                "8",
                "City Hospital",
                "Pune",
                "Doctor@123"
        );
        registrationPage.acceptTerms();
    }

    @Then("the doctor registration submit button should be enabled")
    public void theDoctorRegistrationSubmitButtonShouldBeEnabled() {
        Assert.assertFalse(registrationPage.isCreateAccountDisabled(), "Doctor registration submit button is disabled");
    }
}
