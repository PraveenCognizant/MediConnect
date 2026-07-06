package com.mediconnect.tests;

import com.mediconnect.base.BaseTest;
import com.mediconnect.base.DriverFactory;
import com.mediconnect.pages.AdminDoctorApprovalsPage;
import com.mediconnect.pages.LoginPage;
import com.mediconnect.pages.RegistrationPage;
import com.mediconnect.dataprovider.JsonDataProviders;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DoctorRegistrationApprovalTest extends BaseTest {

    @Test(dataProvider = "doctorRegistrationApprovalData", dataProviderClass = JsonDataProviders.class)
    public void doctorCanRegisterAndAdminCanApproveDoctor(
            String doctorName,
            String doctorEmail,
            String gender,
            String mobile,
            String specialization,
            String experience,
            String previousHospital,
            String address,
            String doctorPassword,
            String adminEmail,
            String adminPassword
    ) {
        RegistrationPage registrationPage = new RegistrationPage(DriverFactory.getDriver()).open();
        registrationPage.fillDoctorRegistration(
                doctorName,
                doctorEmail,
                gender,
                mobile,
                specialization,
                experience,
                previousHospital,
                address,
                doctorPassword
        );
        registrationPage.acceptTerms();
        Assert.assertFalse(registrationPage.isCreateAccountDisabled(), "Doctor registration submit should be enabled");
        registrationPage.submitRegistration();
        Assert.assertTrue(
                registrationPage.isOnPath("/doctordashboard"),
                "New doctor should be registered and redirected to doctor dashboard"
        );

        LoginPage loginPage = new LoginPage(DriverFactory.getDriver()).openLoggedOut();
        loginPage.loginAsAdmin(adminEmail, adminPassword);
        Assert.assertTrue(loginPage.isOnPath("/admindashboard"), "Admin should login before approving doctor");

        AdminDoctorApprovalsPage approvalsPage = new AdminDoctorApprovalsPage(DriverFactory.getDriver()).open();
        Assert.assertTrue(
                approvalsPage.waitForDoctorRow(doctorEmail),
                "Registered doctor should appear in admin approvals: " + doctorEmail
                        + ". Current table: " + approvalsPage.tableText()
        );

        approvalsPage.approveDoctor(doctorEmail);
        Assert.assertTrue(
                approvalsPage.isDoctorAccepted(doctorEmail),
                "Admin should approve registered doctor: " + doctorEmail
        );
    }
}
