package com.mediconnect.steps;

import com.mediconnect.base.DriverFactory;
import com.mediconnect.dataprovider.JsonDataProviders;
import com.mediconnect.pages.RegistrationPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class RegistrationSteps {
    private RegistrationPage registrationPage;

    private RegistrationPage getRegistrationPage() {
        if (registrationPage == null) {
            registrationPage = new RegistrationPage(DriverFactory.getDriver());
        }
        return registrationPage;
    }

    @Given("I open the registration page")
    public void iOpenTheRegistrationPage() {
        registrationPage = new RegistrationPage(DriverFactory.getDriver()).open();
    }

    @When("I fill valid patient registration details")
    public void iFillValidPatientRegistrationDetails() {
        Object[][] data = JsonDataProviders.patientRegistrationData();
        getRegistrationPage().fillUserRegistration(
                (String) data[0][0], // name
                (String) data[0][1], // email
                (String) data[0][2], // gender
                (String) data[0][3], // age
                (String) data[0][4], // mobile
                (String) data[0][5], // address
                (String) data[0][6]  // password
        );
        getRegistrationPage().acceptTerms();
    }

    @Then("the patient registration submit button should be enabled")
    public void thePatientRegistrationSubmitButtonShouldBeEnabled() {
        Assert.assertFalse(getRegistrationPage().isCreateAccountDisabled(), "Patient registration submit button is disabled");
    }

    @When("I fill valid doctor registration details")
    public void iFillValidDoctorRegistrationDetails() {
        Object[][] data = JsonDataProviders.doctorRegistrationApprovalData();
        getRegistrationPage().fillDoctorRegistration(
                (String) data[0][0], // name
                (String) data[0][1], // email
                (String) data[0][2], // gender
                (String) data[0][3], // mobile
                (String) data[0][4], // specialization
                (String) data[0][5], // experience
                (String) data[0][6], // previousHospital
                (String) data[0][7], // address
                (String) data[0][8]  // password
        );
        getRegistrationPage().acceptTerms();
    }

    @Then("the doctor registration submit button should be enabled")
    public void theDoctorRegistrationSubmitButtonShouldBeEnabled() {
        Assert.assertFalse(getRegistrationPage().isCreateAccountDisabled(), "Doctor registration submit button is disabled");
    }
}
