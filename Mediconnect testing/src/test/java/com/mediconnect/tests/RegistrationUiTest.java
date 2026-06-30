package com.mediconnect.tests;

import com.mediconnect.base.BaseTest;
import com.mediconnect.base.DriverFactory;
import com.mediconnect.pages.RegistrationPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegistrationUiTest extends BaseTest {
    @Test
    public void validPatientRegistrationEnablesSubmit() {
        RegistrationPage registrationPage = new RegistrationPage(DriverFactory.getDriver()).open();

        registrationPage.fillUserRegistration(
                "Priya Patient",
                "priya.patient@example.com",
                "Female",
                "28",
                "9876543210",
                "Mumbai",
                "Patient@123"
        );
        registrationPage.acceptTerms();

        Assert.assertFalse(registrationPage.isCreateAccountDisabled(), "Create Account should be enabled");
    }

    @Test
    public void doctorTabShowsDoctorRegistrationForm() {
        RegistrationPage registrationPage = new RegistrationPage(DriverFactory.getDriver()).open();

        registrationPage.chooseDoctorTab();

        Assert.assertEquals(registrationPage.title(), "Doctor Registration");
    }
}
