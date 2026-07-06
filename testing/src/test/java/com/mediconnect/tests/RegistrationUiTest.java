package com.mediconnect.tests;

import com.mediconnect.base.BaseTest;
import com.mediconnect.base.DriverFactory;
import com.mediconnect.pages.RegistrationPage;
import com.mediconnect.dataprovider.JsonDataProviders;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegistrationUiTest extends BaseTest {
    @Test(dataProvider = "patientRegistrationData", dataProviderClass = JsonDataProviders.class)
    public void validPatientRegistrationEnablesSubmit(
            String name,
            String email,
            String gender,
            String age,
            String mobile,
            String address,
            String password
    ) {
        RegistrationPage registrationPage = new RegistrationPage(DriverFactory.getDriver()).open();

        registrationPage.fillUserRegistration(
                name,
                email,
                gender,
                age,
                mobile,
                address,
                password
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
