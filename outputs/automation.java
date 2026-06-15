Here is a complete guide and Selenium Java automation framework structure for testing a **Login Page**. 

This solution uses the **Page Object Model (POM)** design pattern, **TestNG** for test execution and assertions, and follows industry best practices (such as Explicit Waits and Data-Driven Testing).

---

### Part 1: Functional Test Cases

Before writing the automation code, here are the functional test cases for the Login Page.

| Test Case ID | Test Case Description | Input Data | Expected Result |
| :--- | :--- | :--- | :--- |
| **TC_01** | Verify UI elements are present | N/A | Username, Password fields, and Login button are visible. |
| **TC_02** | Successful login with valid credentials | Username: `student`<br>Password: `Password123` | User is redirected to the dashboard/home page. |
| **TC_03** | Unsuccessful login with invalid username | Username: `invalidUser`<br>Password: `Password123` | Error message displayed: "Your username is invalid!" |
| **TC_04** | Unsuccessful login with invalid password | Username: `student`<br>Password: `wrongPassword` | Error message displayed: "Your password is invalid!" |
| **TC_05** | Unsuccessful login with empty fields | Username: ` `<br>Password: ` ` | Error message displayed requesting input. |

---

### Part 2: Automation Framework Setup

#### 1. Maven Dependencies (`pom.xml`)
Ensure you have the following dependencies in your `pom.xml`:

```xml
<dependencies>
    <!-- Selenium WebDriver -->
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.18.1</version>
    </dependency>

    <!-- TestNG -->
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>7.9.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

### Part 3: Automation Script Implementation

We will divide the code into three parts:
1. **Page Object Class (`LoginPage.java`)** - Contains locators and actions.
2. **Base Test Class (`BaseTest.java`)** - Handles browser setup and teardown.
3. **Test Class (`LoginTest.java`)** - Contains the actual TestNG test scripts.

#### 1. Page Object Class (`LoginPage.java`)

```java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // 1. Locators (Using By objects)
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("submit");
    private By errorMessage = By.id("error");
    private By successMessage = By.id("success"); // Assuming successful login lands on a page with this ID

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 2. Actions (Methods)
    
    public boolean isUsernameFieldDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).isDisplayed();
    }

    public boolean isPasswordFieldDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).isDisplayed();
    }

    public boolean isLoginButtonDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton)).isDisplayed();
    }

    public void enterUsername(String username) {
        WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        usernameElement.clear();
        usernameElement.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passwordElement.clear();
        passwordElement.sendKeys(password);
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    // Combined Login Action
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    public boolean isLoginSuccessful() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
```

#### 2. Base Test Class (`BaseTest.java`)

```java
package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected final String BASE_URL = "https://practicetestautomation.com/practice-test-login/"; // Example Practice URL

    @BeforeMethod
    public void setUp() {
        // Initialize ChromeOptions (e.g., start maximized)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        
        // Initialize WebDriver (Selenium 4 automatically manages drivers)
        driver = new ChromeDriver(options);
        
        // Implicit wait as a backup, though Explicit wait is preferred in Page Objects
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        
        // Navigate to the Web Application
        driver.get(BASE_URL);
    }

    @AfterMethod
    public void tearDown() {
        // Close browser sessions
        if (driver != null) {
            driver.quit();
        }
    }
}
```

#### 3. Test Class (`LoginTest.java`)

```java
package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "TC_01: Verify Login UI Elements are displayed")
    public void testUIElementsPresence() {
        LoginPage loginPage = new LoginPage(driver);
        
        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(), "Username field is not displayed.");
        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(), "Password field is not displayed.");
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button is not displayed.");
    }

    @Test(priority = 2, description = "TC_02: Successful login with valid credentials")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);
        
        // Perform login
        loginPage.login("student", "Password123");
        
        // Assert successful redirection / welcome message
        Assert.assertTrue(driver.getCurrentUrl().contains("logged-in-successfully"), "URL does not contain expected success path.");
    }

    // DataProvider for negative login test cases
    @DataProvider(name = "invalidLoginData")
    public Object[][] getInvalidLoginData() {
        return new Object[][] {
            {"invalidUser", "Password123", "Your username is invalid!"}, // TC_03
            {"student", "wrongPassword", "Your password is invalid!"}    // TC_04
        };
    }

    @Test(priority = 3, dataProvider = "invalidLoginData", description = "TC_03 & TC_04: Negative login test cases")
    public void testUnsuccessfulLogin(String username, String password, String expectedErrorMessage) {
        LoginPage loginPage = new LoginPage(driver);
        
        // Perform login with bad data
        loginPage.login(username, password);
        
        // Assert correct error message is displayed
        String actualErrorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(actualErrorMessage.contains(expectedErrorMessage), 
                "Expected error message: '" + expectedErrorMessage + "' but got: '" + actualErrorMessage + "'");
    }
}
```

---

### Best Coding Practices Applied:
1. **Page Object Model (POM):** Separated elements/actions (`LoginPage.java`) from the actual test execution (`LoginTest.java`). This makes code highly maintainable.
2. **Explicit Waits (`WebDriverWait`):** Replaced unreliable `Thread.sleep()` with dynamic waits to handle element loading lag.
3. **Data-Driven Testing (`@DataProvider`):** Grouped negative test cases into a single test method using TestNG DataProviders, preventing code duplication.
4. **Inheritance:** Handled browser initialization and cleanup inside `BaseTest` to keep individual test classes clean.
5. **No Hardcoded Values:** Drivers and configurations are handled dynamically using Selenium 4's built-in driver manager.