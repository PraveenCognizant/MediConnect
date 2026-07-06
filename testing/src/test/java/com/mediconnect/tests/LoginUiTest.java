package com.mediconnect.tests;

import com.mediconnect.base.BaseTest;
import com.mediconnect.base.DriverFactory;
import com.mediconnect.pages.LoginPage;
import com.mediconnect.dataprovider.JsonDataProviders;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginUiTest extends BaseTest {


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

    @Test(dataProvider = "validCredentials", dataProviderClass = JsonDataProviders.class)
    public void validUserCredentialsNavigateToDashboard(String role, String email, String password, String expectedPath) {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver()).open();

        loginPage.loginAsRole(role, email, password);

        Assert.assertTrue(
                loginPage.isOnPath(expectedPath),
                role + " should navigate to " + expectedPath + " after valid login. Current URL: "
                        + loginPage.currentUrl()
        );
    }

    @Test(dataProvider = "invalidCredentials", dataProviderClass = JsonDataProviders.class)
    public void invalidCredentialsShowLoginError(String role, String email, String password, String expectedMessage) {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver()).open();

        loginPage.loginAsRole(role, email, password);

        Assert.assertTrue(
                loginPage.errorMessage().contains(expectedMessage),
                role + " invalid login should show an error message"
        );
    }

    @Test(dataProvider = "invalidEmailFormats", dataProviderClass = JsonDataProviders.class)
    public void invalidEmailFormatsKeepSignInDisabled(String role, String email, String password) {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver()).open();

        loginPage.enterCredentialsForRoleWithoutSubmitting(role, email, password);

        Assert.assertTrue(
                loginPage.isSignInDisabled(),
                role + " Sign In should stay disabled for invalid email format"
        );
    }
}
