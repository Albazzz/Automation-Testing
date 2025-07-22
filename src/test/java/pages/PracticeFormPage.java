package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class PracticeFormPage extends BasePage {

    public PracticeFormPage(WebDriver driver) {
        super(driver);
    }

    // Locators (Verified with HTML)
    private By firstName = By.id("firstName");
    private By lastName = By.id("lastName");
    private By email = By.id("userEmail");
    private By mobile = By.id("userNumber");
    private By dob = By.id("dateOfBirthInput");
    private By upload = By.id("uploadPicture");
    private By address = By.id("currentAddress");
    private By loginButton = By.id("submit");
    private By genderRadios = By.name("gender");
    private By subjectsInput = By.id("subjectsInput");
    private By stateDropdown = By.id("state");
    private By cityDropdown = By.id("city");

    // Getters
    public By getFirstName() { return firstName; }
    public By getLastName() { return lastName; }
    public By getEmail() { return email; }
    public By getMobile() { return mobile; }
    public By getDob() { return dob; }
    public By getUpload() { return upload; }
    public By getAddress() { return address; }
    public By getLoginButton() { return loginButton; }

    public void setDob(String date) {
        WebElement dobInput = waitForVisibility(dob);
        dobInput.click();
        dobInput.sendKeys(Keys.CONTROL + "a");
        dobInput.sendKeys(date);
        dobInput.sendKeys(Keys.ENTER);
    }

    public void clickOutside() {
        WebElement header = driver.findElement(By.id("dateOfBirth-label"));
        header.click();
    }

    public void selectGender(String genderValue) {
        By genderLabel = By.xpath("//label[text()='" + genderValue + "']");
        scrollToElement(genderLabel);
        click(genderLabel);
    }

    public void selectHobbyByLabel(String hobbyLabel) {
        By hobbyCheckbox = By.xpath("//label[text()='" + hobbyLabel + "']");
        scrollToElement(hobbyCheckbox);
        click(hobbyCheckbox);
    }

    public void selectState(String stateName) {
        scrollToElement(stateDropdown);
        click(stateDropdown);
        WebElement input = waitForVisibility(By.id("react-select-3-input"));
        input.sendKeys(stateName);
        input.sendKeys(Keys.ENTER);
    }

    public void selectCity(String cityName) {
        scrollToElement(cityDropdown);
        click(cityDropdown);
        WebElement input = waitForVisibility(By.id("react-select-4-input"));
        input.sendKeys(cityName);
        input.sendKeys(Keys.ENTER);
    }

    public boolean isFieldValid(By locator) {
        WebElement field = waitForVisibility(locator);
        return (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].checkValidity();", field);
    }

    public String getValidationMessage(By locator) {
        WebElement field = waitForVisibility(locator);
        return (String) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].validationMessage;", field);
    }

    public boolean isSubjectValid(String subject) {
        List<WebElement> selectedSubjects = driver.findElements(By.cssSelector(".subjects-auto-complete__multi-value__label"));
        for (WebElement selected : selectedSubjects) {
            if (selected.getText().equalsIgnoreCase(subject)) {
                return true;
            }
        }
        return false;
    }

    public void navigate() {
        navigateTo("https://demoqa.com/automation-practice-form");
    }

    public void selectSubject(String subject) {
        WebElement subjectInput = waitForVisibility(subjectsInput);
        subjectInput.sendKeys(subject);
        subjectInput.sendKeys(Keys.ENTER);
    }

    public void selectSubjects(String... subjects) {
        WebElement subjectInput = waitForVisibility(subjectsInput);
        for (String subject : subjects) {
            subjectInput.sendKeys(subject);
            subjectInput.sendKeys(Keys.ENTER);
        }
    }

    public void removeSubject(String subjectLabel) {
        List<WebElement> subjects = driver.findElements(By.className("subjects-auto-complete__multi-value"));
        for (WebElement subject : subjects) {
            WebElement label = subject.findElement(By.className("subjects-auto-complete__multi-value__label"));
            if (label.getText().equalsIgnoreCase(subjectLabel)) {
                WebElement removeBtn = subject.findElement(By.className("subjects-auto-complete__multi-value__remove"));
                removeBtn.click();
                break;
            }
        }
    }

    public String getMessageText(By locator) {
        return getText(locator);
    }

    private void scrollToElement(By locator) {
        WebElement element = waitForVisibility(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
    }

    public WebElement waitForVisibility(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            System.out.println("Element not visible: " + locator);
            return null;
        }
    }
}