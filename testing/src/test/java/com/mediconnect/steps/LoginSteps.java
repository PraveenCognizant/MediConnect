package com.mediconnect.steps;

import com.mediconnect.base.DriverFactory;
import com.mediconnect.dataprovider.JsonDataProviders;
import com.mediconnect.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginSteps {
    private LoginPage loginPage;

    private LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(DriverFactory.getDriver());
        }
        return loginPage;
    }

    @Then("the patient login form should be displayed")
    public void thePatientLoginFormShouldBeDisplayed() {
        Assert.assertTrue(getLoginPage().isLoaded(), "Login page was not loaded");
        Assert.assertEquals(getLoginPage().title(), "Patient Login");
    }

    @When("I switch to the {string} login tab")
    public void iSwitchToTheLoginTab(String role) {
        if ("doctor".equalsIgnoreCase(role)) {
            getLoginPage().chooseDoctorTab();
        } else if ("admin".equalsIgnoreCase(role)) {
            getLoginPage().chooseAdminTab();
        } else {
            getLoginPage().choosePatientTab();
        }
    }

    @Then("the login form title should be {string}")
    public void theLoginFormTitleShouldBe(String expectedTitle) {
        Assert.assertEquals(getLoginPage().title(), expectedTitle);
    }

    @Given("I open the login page")
    public void iOpenTheLoginPage() {
        loginPage = new LoginPage(DriverFactory.getDriver()).open();
    }

    @When("I login as {string} with email {string} and password {string}")
    public void iLoginAsWithEmailAndPassword(String role, String email, String password) {
        getLoginPage().loginAsRole(role, email, password);
    }

    @When("I enter invalid email {string} for {string} login")
    public void iEnterInvalidEmailForLogin(String email, String role) {
        String password = "";
        Object[][] validCreds = JsonDataProviders.validCredentials();
        for (Object[] row : validCreds) {
            if (role.equalsIgnoreCase((String) row[0])) {
                password = (String) row[2];
                break;
            }
        }
        
        getLoginPage().enterCredentialsForRoleWithoutSubmitting(role, email, password);
    }

    @Then("I should be redirected to {string}")
    public void iShouldBeRedirectedTo(String expectedPath) {
        Assert.assertTrue(getLoginPage().isOnPath(expectedPath), "Expected URL path was not reached: " + expectedPath);
    }

    @Then("a login error containing {string} should be shown")
    public void aLoginErrorContainingShouldBeShown(String expectedMessage) {
        Assert.assertTrue(getLoginPage().errorMessage().contains(expectedMessage), "Expected login error was not shown");
    }

    @Then("the login submit button should be disabled")
    public void theLoginSubmitButtonShouldBeDisabled() {
        Assert.assertTrue(getLoginPage().isSignInDisabled(), "Login submit button should be disabled");
    }
}
