package tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import pages.PracticeFormPage;
import static org.junit.jupiter.api.Assertions.*;

public class PracticeFormTest extends BaseTest {
    private PracticeFormPage registerPage;

    public static List<Object[]> readCsvData() throws IOException {
        List<Object[]> testData = new ArrayList<>();
        String csvFile = "src/test/resources/register-data.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] data = line.split(",", -1);
                testData.add(data);
            }
        }
        return testData;
    }

    @ParameterizedTest
    @MethodSource("readCsvData")
    public void testRegistrationForm(
            String firstName, String lastName, String email, String gender, String mobile,
            String dob, String subjects, String hobbies, String picture, String address,
            String state, String city, String expectedResult) {

        registerPage = new PracticeFormPage(driver);
        registerPage.navigate();
        System.out.println("Current URL: " + driver.getCurrentUrl());

        try {
            if (!firstName.isEmpty()) registerPage.type(registerPage.getFirstName(), firstName);
            if (!lastName.isEmpty()) registerPage.type(registerPage.getLastName(), lastName);
            if (!email.isEmpty()) {
                registerPage.type(registerPage.getEmail(), email);
            }
            if (!gender.isEmpty()) registerPage.selectGender(gender);
            if (!mobile.isEmpty()) registerPage.type(registerPage.getMobile(), mobile);
            if (!dob.isEmpty()) {
                registerPage.setDob(dob);
                registerPage.clickOutside();
            }
            if (!subjects.isEmpty()) {
                String[] subjectArray = subjects.split(";");
                registerPage.selectSubjects(subjectArray);
            }
            if (!hobbies.isEmpty()) {
                String[] hobbyArray = hobbies.split(";");
                for (String hobby : hobbyArray) {
                    registerPage.selectHobbyByLabel(hobby);
                }
            }
            if (!picture.isEmpty()) {
                String absolutePath = Paths.get("src/test/resources/" + picture).toAbsolutePath().toString();
                File file = new File(absolutePath);
                if (!file.exists()) {
                    System.out.println("File does not exist: " + absolutePath);
                    throw new IOException("File does not exist: " + absolutePath);
                }
                registerPage.type(registerPage.getUpload(), absolutePath);
            }
            if (!address.isEmpty()) registerPage.type(registerPage.getAddress(), address);
            if (!state.isEmpty()) registerPage.selectState(state);
            if (!city.isEmpty()) registerPage.selectCity(city);

            registerPage.click(registerPage.getLoginButton());
            boolean isFormValid = driver.findElements(By.cssSelector(".was-validated .form-control:invalid")).isEmpty();
            boolean isSuccess = registerPage.waitForVisibility(By.id("example-modal-sizes-title-lg")) != null;
            System.out.println("Confirmation modal displayed: " + isSuccess);

            if ("success".equalsIgnoreCase(expectedResult)) {
                assertTrue(isSuccess, "Registration form failed despite valid data.");
            } else {
                assertFalse(isSuccess, "Registration form succeeded despite invalid data.");
                if (!email.isEmpty()) {
                    boolean isEmailValid = registerPage.isFieldValid(registerPage.getEmail());
                    String msg = registerPage.getValidationMessage(registerPage.getEmail());
                    System.out.println("Email valid? " + isEmailValid + " | Message: " + msg);
                    if (!isEmailValid) {
                        assertFalse(isSuccess, "Invalid email but form submitted successfully.");
                    }
                }
                if (!mobile.isEmpty()) {
                    boolean isMobileValid = registerPage.isFieldValid(registerPage.getMobile());
                    String msg = registerPage.getValidationMessage(registerPage.getMobile());
                    System.out.println("Mobile valid? " + isMobileValid + " | Message: " + msg);
                    if (!isMobileValid) {
                        assertFalse(isSuccess, "Invalid mobile number but form submitted successfully.");
                    }
                }
                if (!subjects.isEmpty()) {
                    String[] subjectArray = subjects.split(";");
                    for (String subject : subjectArray) {
                        boolean isSubjectValid = registerPage.isSubjectValid(subject);
                        System.out.println("Subject " + subject + " valid? " + isSubjectValid);
                        if (!isSubjectValid) {
                            assertFalse(isSuccess, "Invalid subject but form submitted successfully.");
                        }
                    }
                }
                if (!gender.isEmpty()) {
                    boolean isGenderSelected = !driver.findElements(By.cssSelector("input[name='gender']:checked")).isEmpty();
                    System.out.println("Gender selected? " + isGenderSelected);
                    if (!isGenderSelected) {
                        assertFalse(isSuccess, "Gender not selected but form submitted successfully.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Test failed for data: " + firstName + ", " + email + ". Error: " + e.getMessage());
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            System.out.println("Screenshot saved at: " + screenshot.getAbsolutePath());
            try {
                throw e;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}