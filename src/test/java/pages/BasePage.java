package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public WebElement waitForVisibility(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for element: " + locator);
            throw e;
        }
    }

    public void click(By locator) {
        try {
            waitForVisibility(locator).click();
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for clickable element: " + locator);
            throw e;
        }
    }

    public void type(By locator, String text) {
        try {
            WebElement element = waitForVisibility(locator);
            element.clear();
            element.sendKeys(text);
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting to type into element: " + locator);
            throw e;
        }
    }

    protected String getText(By locator) {
        try {
            return waitForVisibility(locator).getText();
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting to get text from element: " + locator);
            throw e;
        }
    }

    public void navigateTo(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState == 'complete'"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("practice-form-wrapper")));
    }

    public boolean isElementVisible(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}