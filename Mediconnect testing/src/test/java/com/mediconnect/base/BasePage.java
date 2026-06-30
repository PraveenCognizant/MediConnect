package com.mediconnect.base;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    protected void click(WebElement element) {
        WebElement clickable = wait.until(ExpectedConditions.elementToBeClickable(element));
        scrollTo(clickable);
        try {
            clickable.click();
        } catch (ElementClickInterceptedException ex) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickable);
        }
    }

    protected void type(WebElement element, String value) {
        WebElement ready = wait.until(ExpectedConditions.visibilityOf(element));
        ready.clear();
        ready.sendKeys(value);
    }

    protected String textOf(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText();
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (RuntimeException ex) {
            return false;
        }
    }

    protected void selectByVisibleText(WebElement element, String text) {
        new Select(wait.until(ExpectedConditions.visibilityOf(element))).selectByVisibleText(text);
    }

    protected void scrollTo(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    protected void setInputValue(WebElement element, String value) {
        WebElement ready = wait.until(ExpectedConditions.visibilityOf(element));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];"
                        + "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
                        + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                ready,
                value
        );
    }
}
