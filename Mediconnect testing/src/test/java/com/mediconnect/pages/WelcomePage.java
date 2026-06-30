package com.mediconnect.pages;

import com.mediconnect.base.BasePage;
import com.mediconnect.base.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class WelcomePage extends BasePage {
    @FindBy(css = ".welcome-brand")
    private WebElement brand;

    @FindBy(css = ".hero-title")
    private WebElement heroTitle;

    @FindBy(css = ".nav-link-btn[routerlink='/login'], a[href='/login'].nav-link-btn")
    private WebElement loginNavButton;

    @FindBy(css = ".nav-cta-btn[routerlink='/registration'], a[href='/registration'].nav-cta-btn")
    private WebElement signUpNavButton;

    @FindBy(css = ".hero-btn-primary[routerlink='/registration'], a[href='/registration'].hero-btn-primary")
    private WebElement getStartedButton;

    @FindBy(id = "features")
    private WebElement featuresSection;

    public WelcomePage(WebDriver driver) {
        super(driver);
    }

    public WelcomePage open() {
        driver.get(DriverFactory.baseUrl());
        return this;
    }

    public boolean isLoaded() {
        return isDisplayed(brand) && textOf(heroTitle).contains("Your Health");
    }

    public LoginPage goToLogin() {
        click(loginNavButton);
        return new LoginPage(driver);
    }

    public RegistrationPage goToRegistrationFromNav() {
        click(signUpNavButton);
        return new RegistrationPage(driver);
    }

    public RegistrationPage goToRegistrationFromHero() {
        click(getStartedButton);
        return new RegistrationPage(driver);
    }

    public void scrollToFeatures() {
        scrollTo(featuresSection);
    }

    public boolean isFeaturesSectionVisible() {
        return isDisplayed(featuresSection);
    }
}
