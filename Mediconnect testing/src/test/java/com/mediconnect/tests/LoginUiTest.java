package com.mediconnect.tests;

import com.mediconnect.base.BaseTest;
import com.mediconnect.base.DriverFactory;
import com.mediconnect.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginUiTest extends BaseTest {
    @DataProvider(name = "validCredentials")
    public Object[][] validCredentials() {
        return new Object[][]{
                {"patient", "john@gmail.com", "John@123", "/userdashboard"},
                {"doctor", "praveendu@gmail.com", "Praveen@123", "/doctordashboard"},
                {"admin", "admin@mediconnect.com", "Admin@123", "/admindashboard"}
        };
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][]{
                {"patient", "john@gmail.com", "Wrong@123", "Bad credentials"},
                {"patient", "unknown.patient@example.com", "John@123", "Bad credentials"},
                {"doctor", "praveendu@gmail.com", "Wrong@123", "Bad credentials"},
                {"doctor", "unknown.doctor@example.com", "Praveen@123", "Bad credentials"},
                {"admin", "admin@mediconnect.com", "Wrong@123", "Bad admin credentials"},
                {"admin", "unknown.admin@example.com", "Admin@123", "Bad admin credentials"}
        };
    }

    @DataProvider(name = "invalidEmailFormats")
    public Object[][] invalidEmailFormats() {
        return new Object[][]{
                {"patient", "john", "John@123"},
                {"doctor", "praveendu", "Praveen@123"},
                {"admin", "admin", "Admin@123"}
        };
    }

    @Test
    public void loginRoleTabsDisplayCorrectForms() {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver()).open();

        Assert.assertEquals(loginPage.title(), "Patient Login");

        loginPage.chooseDoctorTab();
        Assert.assertEquals(loginPage.title(), "Doctor Login");

        loginPage.chooseAdminTab();
        Assert.assertEquals(loginPage.title(), "Admin Login");
    }

    @Test
    public void invalidPatientEmailKeepsSignInDisabled() {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver()).open();

        loginPage.enterPatientEmail("invalid-email");

        Assert.assertTrue(loginPage.isSignInDisabled(), "Sign In should stay disabled for invalid email");
    }

    @Test(dataProvider = "validCredentials")
    public void validUserCredentialsNavigateToDashboard(String role, String email, String password, String expectedPath) {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver()).open();

        loginPage.loginAsRole(role, email, password);

        Assert.assertTrue(
                loginPage.isOnPath(expectedPath),
                role + " should navigate to " + expectedPath + " after valid login. Current URL: "
                        + loginPage.currentUrl()
        );
    }

    @Test(dataProvider = "invalidCredentials")
    public void invalidCredentialsShowLoginError(String role, String email, String password, String expectedMessage) {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver()).open();

        loginPage.loginAsRole(role, email, password);

        Assert.assertTrue(
                loginPage.errorMessage().contains(expectedMessage),
                role + " invalid login should show an error message"
        );
    }

    @Test(dataProvider = "invalidEmailFormats")
    public void invalidEmailFormatsKeepSignInDisabled(String role, String email, String password) {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver()).open();

        loginPage.enterCredentialsForRoleWithoutSubmitting(role, email, password);

        Assert.assertTrue(
                loginPage.isSignInDisabled(),
                role + " Sign In should stay disabled for invalid email format"
        );
    }
}
