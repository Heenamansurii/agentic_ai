Here is a complete guide containing **Functional Test Cases** and a **Selenium Java Automation Script** using the **Page Object Model (POM)** pattern, which is the industry standard for maintainable automation.

---

# Part 1: Functional Test Cases for Login Page

| TC ID | Test Case Description | Test Steps | Expected Result |
| :--- | :--- | :--- | :--- |
| **TC_01** | Verify successful login with valid credentials (Positive) | 1. Enter valid username.<br>2. Enter valid password.<br>3. Click 'Login' button. | User is redirected to the Dashboard/Home page. |
| **TC_02** | Verify login fails with invalid username and valid password | 1. Enter invalid username.<br>2. Enter valid password.<br>3. Click 'Login' button. | Error message displayed: "Invalid username or password." |
| **TC_03** | Verify login fails with valid username and invalid password | 1. Enter valid username.<br>2. Enter invalid password.<br>3. Click 'Login' button. | Error message displayed: "Invalid username or password." |
| **TC_04** | Verify login fails with empty fields | 1. Leave username and password empty.<br>2. Click 'Login' button. | Validation message displayed: "Username/Password cannot be empty." |
| **TC_05** | Verify password masking (Security) | 1. Type characters in the password field. | Password characters should be masked (displayed as dots/asterisks `*`). |
| **TC_06** | Verify login functionality using 'Enter' key | 1. Enter valid username and password.<br>2. Press 'Enter' key on the keyboard. | User is redirected to the Dashboard/Home page. |

---

# Part 2: Selenium Java Automation Script

This implementation uses **Selenium 4**, **Java**, and **TestNG** framework under the **Page Object Model (POM)** architecture.

### 1. Maven Dependencies (`pom.xml`)
Add these dependencies to your Maven project:
```xml
<dependencies>
    <!-- Selenium Java -->
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

### 2. Page Object Class (`LoginPage.java`)
This class stores the locators and actions for the Login Page.

```java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // 1. Locators
    private By usernameField = By.id("username"); // Change ID as per your DOM
    private By passwordField = By.id("password"); // Change ID as per your DOM
    private By loginButton = By.id("submit");     // Change ID as per your DOM
    private By errorMessage = By.id("error-msg");  // Change ID as per your DOM

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 2. Actions/Methods
    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).clear();
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLoginButton() {
        driver.findElement(loginButton).click();
    }

    // Combined action helper
    public void login(String username, String password) {
        this.enterUsername(username);
        this.enterPassword(password);
        this.clickLoginButton();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }
}
```

---

### 3. Test Class (`LoginTest.java`)
This class contains the actual test assertions matching the test cases.

```java
package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private final String BASE_URL = "https://example.com/login"; // Replace with your target URL

    @BeforeMethod
    public void setUp() {
        // WebDriverManager is built-in inside Selenium 4. No need for System.setProperty
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL);
        loginPage = new LoginPage(driver);
    }

    @Test(priority = 1)
    public void testSuccessfulLogin() {
        // TC_01: Valid credentials
        loginPage.login("validUser", "validPassword123");
        
        // Assert redirect to Dashboard (adjust URL/Page Title as required)
        String expectedUrl = "https://example.com/dashboard";
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "Login failed with valid credentials!");
    }

    @Test(priority = 2)
    public void testInvalidUsername() {
        // TC_02: Invalid Username, Valid Password
        loginPage.login("invalidUser", "validPassword123");
        
        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, "Invalid username or password.", "Error message mismatch!");
    }

    @Test(priority = 3)
    public void testInvalidPassword() {
        // TC_03: Valid Username, Invalid Password
        loginPage.login("validUser", "wrongPassword");
        
        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, "Invalid username or password.", "Error message mismatch!");
    }

    @Test(priority = 4)
    public void testEmptyFields() {
        // TC_04: Empty Fields
        loginPage.login("", "");
        
        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, "Username/Password cannot be empty.", "Error message mismatch!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
```

### Note on adaptation:
* Replace `https://example.com/login` with your actual testing URL.
* Inspect your web page and replace the `By.id(...)` locators in `LoginPage.java` with actual elements (using `By.name`, `By.xpath`, or `By.cssSelector` if IDs are not available).