package com.mediconnect.steps;

import com.mediconnect.base.DriverFactory;
import com.mediconnect.pages.WelcomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class WelcomeSteps {
    private WelcomePage welcomePage;

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
        if (welcomePage == null) {
            welcomePage = new WelcomePage(DriverFactory.getDriver());
        }
        welcomePage.goToLogin();
    }
}
