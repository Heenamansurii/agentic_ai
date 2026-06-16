Okay, let's generate Selenium Java automation code for the login page, covering the important scenarios identified in your review.

**Assumptions:**

*   You have a basic understanding of Java and Selenium.
*   You have the Selenium WebDriver and a browser driver (like ChromeDriver) set up.
*   Your login page has predictable element locators (IDs, names, CSS selectors, XPath). For this example, I'll use common locators, but you'll need to adapt them to your actual HTML.
*   The application has a base URL.
*   The application provides clear validation messages.

**Project Structure (Example):**

```
src/
  main/
    java/
      com/example/
        pages/
          LoginPage.java
        tests/
          LoginTest.java
        utils/
          WebDriverManager.java
  test/
    java/
      com/example/
        pages/
          LoginPage.java
        tests/
          LoginTest.java
        utils/
          WebDriverManager.java
```

**1. `WebDriverManager.java` (Utility for setting up WebDriver)**

```java
package com.example.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class WebDriverManager {

    private static WebDriver driver;

    public static void setupDriver() {
        // Using WebDriverManager to automatically download and set up the driver
        WebDriverManager.chromedriver().setup();

        // Optional: Configure ChromeOptions for headless execution, etc.
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // Uncomment for headless browser
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox"); // Bypass OS security model
        options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems

        driver = new ChromeDriver(options);

        // Set implicit wait for all elements
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            setupDriver();
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
```

**2. `LoginPage.java` (Page Object Model for the Login Page)**

```java
package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators (Update these to match your actual HTML)
    private By usernameField = By.id("username"); // Or By.name("username"), By.cssSelector("input[name='username']")
    private By passwordField = By.id("password"); // Or By.name("password"), By.cssSelector("input[name='password']")
    private By loginButton = By.xpath("//button[text()='Login']"); // Or By.cssSelector("button.login-button")

    // Validation Messages Locators (These might vary significantly)
    private By usernameRequiredError = By.cssSelector(".error-message.username-required"); // Example CSS selector
    private By passwordRequiredError = By.cssSelector(".error-message.password-required"); // Example CSS selector
    private By invalidCredentialsError = By.cssSelector(".error-message.invalid-credentials"); // Example CSS selector
    private By accountLockedError = By.cssSelector(".error-message.account-locked"); // Example CSS selector

    // Other UI Elements
    private By forgotPasswordLink = By.linkText("Forgot Password?"); // Example
    private By signUpLink = By.linkText("Sign Up"); // Example
    private By rememberMeCheckbox = By.id("remember-me"); // Example

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Increased wait time for stability
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    public void enterUsername(String username) {
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        usernameInput.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passwordInput.sendKeys(password);
    }

    public void clickLoginButton() {
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginBtn.click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    // --- Validation Methods ---

    public boolean isUsernameRequiredErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameRequiredError));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPasswordRequiredErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(passwordRequiredError));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isInvalidCredentialsErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(invalidCredentialsError));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccountLockedErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(accountLockedError));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Method to get the current URL to check for successful login redirection
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // Method to check if user is on login page (e.g., by checking for username field)
    public boolean isOnLoginPage() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // --- Other Element Interactions ---

    public void clickForgotPasswordLink() {
        WebElement forgotPwdLink = wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink));
        forgotPwdLink.click();
    }

    public void clickSignUpLink() {
        WebElement signUp = wait.until(ExpectedConditions.elementToBeClickable(signUpLink));
        signUp.click();
    }

    public void selectRememberMe(boolean select) {
        WebElement rememberMe = wait.until(ExpectedConditions.elementToBeClickable(rememberMeCheckbox));
        if (rememberMe.isSelected() != select) {
            rememberMe.click();
        }
    }

    public boolean isRememberMeSelected() {
        try {
            return driver.findElement(rememberMeCheckbox).isSelected();
        } catch (Exception e) {
            return false; // Checkbox not found
        }
    }

    // Method to clear fields before entering new values
    public void clearUsernameField() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).clear();
    }

    public void clearPasswordField() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).clear();
    }

    // Method to check if password field is masked (check type attribute)
    public String getPasswordFieldType() {
        return driver.findElement(passwordField).getAttribute("type");
    }

    // Method to check for autocomplete attribute (though often browser-controlled)
    public String getPasswordAutocompleteAttribute() {
        return driver.findElement(passwordField).getAttribute("autocomplete");
    }

    // Add methods for other specific interactions as needed
}
```

**3. `LoginTest.java` (Test Cases using JUnit 5 and the Page Objects)**

```java
package com.example.tests;

import com.example.pages.LoginPage;
import com.example.utils.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // To run tests in a specific order
public class LoginTest {

    private static WebDriver driver;
    private LoginPage loginPage;
    private static final String BASE_URL = "http://your-app-url.com/login"; // *** REPLACE WITH YOUR APP'S LOGIN URL ***
    private static final String VALID_USERNAME = "testuser"; // *** REPLACE WITH YOUR VALID USERNAME ***
    private static final String VALID_PASSWORD = "password123"; // *** REPLACE WITH YOUR VALID PASSWORD ***
    private static final String INVALID_USERNAME = "wronguser";
    private static final String INVALID_PASSWORD = "wrongpassword";

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.setupDriver();
    }

    @BeforeEach
    void setUpEach() {
        driver = WebDriverManager.getDriver();
        loginPage = new LoginPage(driver);
        loginPage.navigateTo(BASE_URL); // Navigate to login page for each test
        // Clear cookies and local storage if needed to ensure a clean state
        driver.manage().deleteAllCookies();
        // In modern JS apps, you might need to clear localStorage/sessionStorage too
    }

    @AfterEach
    void tearDownEach() {
        // No explicit action needed here if using @BeforeEach for navigation
        // but you could add checks for browser logs if needed
    }

    @AfterAll
    static void tearDownAll() {
        WebDriverManager.quitDriver();
    }

    // --- Original Test Cases (TC_LOGIN_001 to TC_LOGIN_005) ---

    @Test
    @Order(1)
    @DisplayName("TC_LOGIN_001: Valid successful login")
    void testSuccessfulLogin() {
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);
        // Assuming a successful login redirects to a dashboard page, check the URL
        assertTrue(loginPage.getCurrentUrl().contains("/dashboard"), "Login should redirect to dashboard");
        // Or assert presence of a dashboard element
    }

    @Test
    @Order(2)
    @DisplayName("TC_LOGIN_002: Invalid username, valid password")
    void testInvalidUsernameValidPassword() {
        loginPage.login(INVALID_USERNAME, VALID_PASSWORD);
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "Invalid credentials error should be displayed");
        assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page");
    }

    @Test
    @Order(3)
    @DisplayName("TC_LOGIN_003: Valid username, invalid password")
    void testValidUsernameInvalidPassword() {
        loginPage.login(VALID_USERNAME, INVALID_PASSWORD);
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "Invalid credentials error should be displayed");
        assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page");
    }

    @Test
    @Order(4)
    @DisplayName("TC_LOGIN_004: Both fields empty")
    void testEmptyCredentials() {
        loginPage.clickLoginButton();
        assertTrue(loginPage.isUsernameRequiredErrorDisplayed(), "Username required error should be displayed");
        assertTrue(loginPage.isPasswordRequiredErrorDisplayed(), "Password required error should be displayed");
        assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page");
    }

    @Test
    @Order(5)
    @DisplayName("TC_LOGIN_005: Username filled, password empty")
    void testUsernameFilledPasswordEmpty() {
        loginPage.enterUsername(VALID_USERNAME);
        loginPage.clickLoginButton();
        assertTrue(loginPage.isPasswordRequiredErrorDisplayed(), "Password required error should be displayed");
        assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page");
    }

    // --- Identified Areas for Improvement Test Cases ---

    @Test
    @Order(6)
    @DisplayName("Missing Scenario 1: Login with Password Only (Username Empty)")
    void testLoginWithPasswordOnly() {
        loginPage.enterPassword(VALID_PASSWORD);
        loginPage.clickLoginButton();
        assertTrue(loginPage.isUsernameRequiredErrorDisplayed(), "Username required error should be displayed");
        assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page");
    }

    @Test
    @Order(7)
    @DisplayName("Missing Scenario 2: Case Sensitivity of Username (assuming case-sensitive)")
    void testCaseSensitiveUsername() {
        String mixedCaseUsername = VALID_USERNAME.substring(0, 1).toUpperCase() + VALID_USERNAME.substring(1); // e.g., "Testuser"
        loginPage.login(mixedCaseUsername, VALID_PASSWORD);
        // This test depends on your application's behavior.
        // If it's case-sensitive, it should fail. If case-insensitive, it should pass.
        // We'll assert failure here as is more common for usernames.
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "Login with incorrect username case should fail");
        assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page");
    }

    @Test
    @Order(8)
    @DisplayName("Missing Scenario 3: Case Sensitivity of Password")
    void testCaseSensitivePassword() {
        String mixedCasePassword = VALID_PASSWORD.substring(0, 1).toUpperCase() + VALID_PASSWORD.substring(1); // e.g., "Password123"
        loginPage.login(VALID_USERNAME, mixedCasePassword);
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "Login with incorrect password case should fail");
        assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page");
    }

    @Test
    @Order(9)
    @DisplayName("Missing Scenario 4.1: 'Remember Me' - Checked, then navigate")
    void testRememberMeChecked() {
        loginPage.selectRememberMe(true);
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);
        assertTrue(loginPage.getCurrentUrl().contains("/dashboard"), "Login should redirect to dashboard");

        // Simulate closing and reopening the browser (by navigating to login page again, assuming cookies persist)
        driver.get(BASE_URL);
        // If cookies are handled correctly, the user should be logged in
        assertTrue(loginPage.getCurrentUrl().contains("/dashboard"), "'Remember Me' should keep user logged in");
    }

    @Test
    @Order(10)
    @DisplayName("Missing Scenario 4.2: 'Remember Me' - Unchecked, then navigate")
    void testRememberMeUnchecked() {
        loginPage.selectRememberMe(false); // Ensure it's unchecked
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);
        assertTrue(loginPage.getCurrentUrl().contains("/dashboard"), "Login should redirect to dashboard");

        // Simulate closing and reopening the browser
        driver.get(BASE_URL);
        // User should be prompted to log in again
        assertTrue(loginPage.isOnLoginPage(), "'Remember Me' unchecked should require re-login");
    }

    @Test
    @Order(11)
    @DisplayName("Missing Scenario 5: 'Forgot Password' Link Navigation")
    void testForgotPasswordLink() {
        loginPage.clickForgotPasswordLink();
        // Assert that the URL has changed to the expected password reset page
        assertTrue(driver.getCurrentUrl().contains("/reset-password"), "Should navigate to password reset page");
        // You might also assert the presence of specific elements on the password reset page
    }

    @Test
    @Order(12)
    @DisplayName("Missing Scenario 6: 'Sign Up' Link Navigation")
    void testSignUpLink() {
        loginPage.clickSignUpLink();
        // Assert that the URL has changed to the expected registration page
        assertTrue(driver.getCurrentUrl().contains("/signup"), "Should navigate to sign up page");
        // You might also assert the presence of specific elements on the sign up page
    }

    @Test
    @Order(13)
    @DisplayName("Missing Scenario 7: Multiple Failed Login Attempts (Account Lockout Simulation)")
    void testMultipleFailedLogins() {
        // Attempt to log in with invalid credentials multiple times
        int failedAttemptsThreshold = 5; // Example threshold for lockout

        for (int i = 0; i < failedAttemptsThreshold; i++) {
            loginPage.login(INVALID_USERNAME + i, INVALID_PASSWORD + i);
            if (i < failedAttemptsThreshold - 1) { // Before the last attempt
                assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "Failed login attempt " + (i + 1) + " should show invalid credentials");
                assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page after failed attempt " + (i + 1));
            }
        }

        // The last attempt should trigger the lockout or specific message
        loginPage.login("final_attempt_user", "final_attempt_pass");
        assertTrue(loginPage.isAccountLockedErrorDisplayed() || loginPage.isInvalidCredentialsErrorDisplayed(),
                "After threshold, account should be locked or show specific message.");
        assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page after lockout attempt");
    }

    @Test
    @Order(14)
    @DisplayName("Missing Scenario 8: Login with Special Characters in Username")
    void testLoginWithSpecialCharsUsername() {
        // Assuming special characters are allowed in username
        String usernameWithSpecialChars = "user@domain!#$"; // Example
        // NOTE: You need to know if your system *actually* supports these chars in username and have corresponding valid credentials.
        // For this test, we'll assume it's an invalid credential scenario if your system doesn't support them.
        // If your system supports it and you have a valid credential, replace with that.
        loginPage.login(usernameWithSpecialChars, VALID_PASSWORD);
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "Login with special chars in username should fail if not supported or invalid");
        assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page");
    }

    @Test
    @Order(15)
    @DisplayName("Missing Scenario 8: Login with Special Characters in Password")
    void testLoginWithSpecialCharsPassword() {
        // Assuming special characters are allowed in password
        String passwordWithSpecialChars = "pass*&^()_+=!@#$"; // Example
        // NOTE: Similar to username, you need to know if your system supports these chars and have corresponding valid credentials.
        loginPage.login(VALID_USERNAME, passwordWithSpecialChars);
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "Login with special chars in password should fail if not supported or invalid");
        assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page");
    }


    // --- Negative Test Cases ---

    @Test
    @Order(16)
    @DisplayName("Negative Test 1: Username exceeds maximum length")
    void testUsernameExceedsMaxLength() {
        // Assuming a max length of, say, 50 characters. Construct a longer string.
        String longUsername = "a".repeat(100);
        loginPage.enterUsername(longUsername);
        loginPage.enterPassword(VALID_PASSWORD);
        loginPage.clickLoginButton();

        // The behavior here can vary:
        // 1. Input field might truncate the input automatically.
        // 2. A validation message might appear on the page.
        // We'll assume it either doesn't allow submission or shows an error.
        // If it truncates and logs in, that's also a valid, albeit poor, scenario.
        assertTrue(loginPage.isOnLoginPage(), "Should remain on login page");
        // You might need a more specific check for a length validation message if your app has one.
        // For now, we check if it *didn't* log in.
        assertFalse(loginPage.getCurrentUrl().contains("/dashboard"), "Login should not succeed with excessively long username");
    }

    @Test
    @Order(17)
    @DisplayName("Negative Test 2: Password exceeds maximum length")
    void testPasswordExceedsMaxLength() {
        String longPassword = "b".repeat(100);
        loginPage.enterUsername(VALID_USERNAME);
        loginPage.enterPassword(longPassword);
        loginPage.clickLoginButton();

        assertTrue(loginPage.isOnLoginPage(), "Should remain on login page");
        assertFalse(loginPage.getCurrentUrl().contains("/dashboard"), "Login should not succeed with excessively long password");
    }

    @Test
    @Order(18)
    @DisplayName("Negative Test 3: Username with leading/trailing spaces")
    void testUsernameWithLeadingTrailingSpaces() {
        String usernameWithSpaces = "  " + VALID_USERNAME + "  ";
        loginPage.login(usernameWithSpaces, VALID_PASSWORD);

        // This depends on application's behavior:
        // - If spaces are trimmed: successful login.
        // - If spaces are part of credentials: failed login.
        // We'll assume it fails if it's not trimmed.
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "Login with leading/trailing spaces in username should fail");
        assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page");
    }

    @Test
    @Order(19)
    @DisplayName("Negative Test 4: Password with leading/trailing spaces")
    void testPasswordWithLeadingTrailingSpaces() {
        String passwordWithSpaces = "  " + VALID_PASSWORD + "  ";
        loginPage.login(VALID_USERNAME, passwordWithSpaces);

        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "Login with leading/trailing spaces in password should fail");
        assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page");
    }

    @Test
    @Order(20)
    @DisplayName("Negative Test 5: Username with control characters")
    void testUsernameWithControlCharacters() {
        String usernameWithControlChars = VALID_USERNAME + "\n"; // Newline character
        loginPage.login(usernameWithControlChars, VALID_PASSWORD);

        assertTrue(loginPage.isOnLoginPage(), "Should remain on login page");
        // Assert that it's not logged in and potentially an error is shown
        assertFalse(loginPage.getCurrentUrl().contains("/dashboard"));
        // You might check for a specific input validation error if your app provides one.
    }

    @Test
    @Order(21)
    @DisplayName("Negative Test 6: Password with control characters")
    void testPasswordWithControlCharacters() {
        String passwordWithControlChars = VALID_PASSWORD + "\t"; // Tab character
        loginPage.login(VALID_USERNAME, passwordWithControlChars);

        assertTrue(loginPage.isOnLoginPage(), "Should remain on login page");
        assertFalse(loginPage.getCurrentUrl().contains("/dashboard"));
    }

    @Test
    @Order(22)
    @DisplayName("Negative Test 7: Username with script tags (XSS attempt)")
    void testUsernameXSSAttempt() {
        String xssInput = "<script>alert('XSS Username')</script>";
        loginPage.login(xssInput, VALID_PASSWORD);

        // The script should NOT execute. If it does, the test will fail because an alert box pops up.
        // A more robust check would be to ensure the input is escaped or rejected.
        // Here, we primarily check that login fails as expected.
        assertTrue(loginPage.isOnLoginPage(), "Should remain on login page");
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "XSS attempt in username should result in invalid credentials error");
        assertFalse(loginPage.getCurrentUrl().contains("/dashboard"), "Login should not succeed with XSS input");
    }

    @Test
    @Order(23)
    @DisplayName("Negative Test 8: Password with script tags (XSS attempt)")
    void testPasswordXSSAttempt() {
        String xssInput = "<script>alert('XSS Password')</script>";
        loginPage.login(VALID_USERNAME, xssInput);

        assertTrue(loginPage.isOnLoginPage(), "Should remain on login page");
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "XSS attempt in password should result in invalid credentials error");
        assertFalse(loginPage.getCurrentUrl().contains("/dashboard"), "Login should not succeed with XSS input");
    }

    // --- Edge Cases ---

    @Test
    @Order(24)
    @DisplayName("Edge Case 1: Username at maximum allowed length")
    void testUsernameAtMaxLength() {
        // Assuming a max length of 50. Adjust if your app has a different limit.
        int maxLength = 50;
        String maxLenUsername = "a".repeat(maxLength);
        loginPage.login(maxLenUsername, VALID_PASSWORD);
        // This test assumes you have a valid username of max length.
        // If this is an actual valid credential, it should pass.
        // If it's just testing the boundary, and the system doesn't have a credential for it, it might fail.
        // Modify assertion based on your system's test data.
        // For demonstration, we assume it might fail if the length causes issues beyond just validation.
        assertTrue(loginPage.isOnLoginPage(), "Should remain on login page if max length username causes issues");
        // If you have a valid credential for this max length username:
        // assertTrue(loginPage.getCurrentUrl().contains("/dashboard"), "Login with max length username should succeed");
    }

    @Test
    @Order(25)
    @DisplayName("Edge Case 2: Password at maximum allowed length")
    void testPasswordAtMaxLength() {
        int maxLength = 50;
        String maxLenPassword = "b".repeat(maxLength);
        loginPage.login(VALID_USERNAME, maxLenPassword);
        // Similar assumption as above for max length username.
        assertTrue(loginPage.isOnLoginPage(), "Should remain on login page if max length password causes issues");
        // If you have a valid credential for this max length password:
        // assertTrue(loginPage.getCurrentUrl().contains("/dashboard"), "Login with max length password should succeed");
    }

    @Test
    @Order(26)
    @DisplayName("Edge Case 3: Special characters as only input (Username)")
    void testUsernameOnlySpecialChars() {
        String usernameWithOnlySpecialChars = "!@#$%^&*()";
        loginPage.login(usernameWithOnlySpecialChars, VALID_PASSWORD);
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "Login with only special chars in username should fail");
        assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page");
    }

    @Test
    @Order(27)
    @DisplayName("Edge Case 4: Special characters as only input (Password)")
    void testPasswordOnlySpecialChars() {
        String passwordWithOnlySpecialChars = "!@#$%^&*()";
        loginPage.login(VALID_USERNAME, passwordWithOnlySpecialChars);
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "Login with only special chars in password should fail");
        assertTrue(loginPage.isOnLoginPage(), "Should remain on the login page");
    }


    // --- Security Test Cases ---

    @Test
    @Order(28)
    @DisplayName("Security Test 1: SQL Injection Attempt (Username)")
    void testSqlInjectionUsername() {
        String sqlInjectionPayload = "' OR '1'='1";
        loginPage.login(sqlInjectionPayload, VALID_PASSWORD);

        assertTrue(loginPage.isOnLoginPage(), "Should remain on login page after SQL injection attempt");
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "SQL injection attempt should result in invalid credentials error");
        assertFalse(loginPage.getCurrentUrl().contains("/dashboard"), "Login should not succeed with SQL injection");
    }

    @Test
    @Order(29)
    @DisplayName("Security Test 2: SQL Injection Attempt (Password)")
    void testSqlInjectionPassword() {
        String sqlInjectionPayload = "' OR '1'='1";
        loginPage.login(VALID_USERNAME, sqlInjectionPayload);

        assertTrue(loginPage.isOnLoginPage(), "Should remain on login page after SQL injection attempt");
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "SQL injection attempt should result in invalid credentials error");
        assertFalse(loginPage.getCurrentUrl().contains("/dashboard"), "Login should not succeed with SQL injection");
    }

    @Test
    @Order(30)
    @DisplayName("Security Test 3: HTTPS Usage")
    void testHttpsUsage() {
        // This test primarily verifies the URL starts with https.
        // For deeper SSL/TLS validation, you'd need more advanced tools or browser capabilities.
        assertTrue(driver.getCurrentUrl().startsWith("https://"), "Login page should be served over HTTPS");
        // If the login submission itself is a POST to a different URL, you'd need to intercept network requests,
        // which is beyond basic Selenium. For basic verification, we check the page's protocol.
    }

    @Test
    @Order(31)
    @DisplayName("Security Test 4: Password Masking")
    void testPasswordMasking() {
        // Enter something in the password field to ensure it's masked
        loginPage.enterPassword("some_password");
        assertEquals("password", loginPage.getPasswordFieldType(), "Password field should mask input");
    }

    @Test
    @Order(32)
    @DisplayName("Security Test 5: Session Management (Logout and Back Button)")
    void testSessionManagementAfterLogout() {
        // First, log in successfully
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);
        assertTrue(loginPage.getCurrentUrl().contains("/dashboard"), "Should be logged in");

        // Simulate logout (assuming a logout link/button exists)
        // If not, you might need to implement a logout method in LoginPage or a separate Page object
        // For now, we'll simulate by clearing cookies and navigating back to login
        driver.manage().deleteAllCookies();
        driver.navigate().refresh(); // Refresh to apply cookie deletion

        // Attempt to navigate back to a supposed authenticated page (if you knew its URL)
        // Or, more simply, verify you are back on the login page.
        loginPage.navigateTo(BASE_URL); // Ensure we are on the login page

        // Simulate using the back button to try and return to the dashboard
        driver.navigate().back(); // Go back to where we were before navigating to BASE_URL

        // After logout and back button, you should NOT be on the dashboard anymore.
        // Ideally, you should be redirected to the login page.
        assertTrue(loginPage.isOnLoginPage(), "After logout and back, user should be on login page");
    }

    @Test
    @Order(33)
    @DisplayName("Security Test 6: Error Message Granularity")
    void testErrorMessageGranularity() {
        // This test is tricky to automate without knowing exact error messages.
        // Scenario: User 'nonexistent_user' exists, password 'wrong_password'
        // Scenario: User 'valid_user' exists, password 'wrong_password'
        // The goal is to ensure the error is generic for both cases.

        // Test with an invalid password for a known valid user
        loginPage.login(VALID_USERNAME, INVALID_PASSWORD);
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "Should show generic invalid credentials error.");

        // Test with a non-existent username and a valid password
        loginPage.navigateTo(BASE_URL); // Go back to login page
        loginPage.login(INVALID_USERNAME, VALID_PASSWORD);
        assertTrue(loginPage.isInvalidCredentialsErrorDisplayed(), "Should show generic invalid credentials error for non-existent user.");

        // IMPORTANT: If your application shows different messages like "User not found" vs "Incorrect password",
        // then these assertions would need to be adjusted to *expect* that generic error.
        // Ideally, you should NOT reveal whether a username exists or not.
    }

    // --- Browser Log Verification (Optional but recommended for security/errors) ---
    // This is more advanced and requires parsing log messages.
    // @Test
    // @DisplayName("Verify No Console Errors During Login")
    // void testNoConsoleErrors() {
    //     loginPage.login(VALID_USERNAME, VALID_PASSWORD);
    //     List<LogEntry> browserConsoleLogs = driver.manage().logs().get(LogType.BROWSER).collect(Collectors.toList());
    //
    //     // Filter out warnings and severe errors if needed, focus on JavaScript errors
    //     List<LogEntry> javascriptErrors = browserConsoleLogs.stream()
    //             .filter(entry -> entry.getLevel().toString().equalsIgnoreCase("SEVERE") || entry.getLevel().toString().equalsIgnoreCase("WARNING"))
    //             .collect(Collectors.toList());
    //
    //     assertTrue(javascriptErrors.isEmpty(), "No browser console errors should occur during login. Errors: " + javascriptErrors);
    // }

    // Add more tests for specific elements like 'Forgot Password' success, 'Sign Up' success, etc.
}
```

**Explanation and How to Use:**

1.  **`WebDriverManager.java`:**
    *   Uses `WebDriverManager` (a fantastic library) to automatically download and set up the correct browser driver (ChromeDriver in this case).
    *   Provides `setupDriver()`, `getDriver()`, and `quitDriver()` methods.
    *   Includes basic `ChromeOptions` for headless mode, which is useful for CI/CD pipelines.

2.  **`LoginPage.java` (Page Object Model - POM):**
    *   **Encapsulation:** This class represents the login page. All interactions with the login page elements (entering text, clicking buttons, getting text) are handled here.
    *   **Locators:** `By` objects are used to locate elements (e.g., `By.id("username")`). **You MUST update these to match your application's HTML.** Use browser developer tools (Inspect Element) to find appropriate IDs, names, CSS selectors, or XPath expressions.
    *   **`WebDriverWait`:** Essential for stable automation. It waits for elements to be present, visible, or clickable before interacting with them, preventing `NoSuchElementException` errors caused by slow page loads.
    *   **Methods:**
        *   `navigateTo(url)`: Opens the login page.
        *   `enterUsername(username)`, `enterPassword(password)`: Enters text into the respective fields.
        *   `clickLoginButton()`: Clicks the login button.
        *   `login(username, password)`: A convenience method to perform a full login action.
        *   Validation Methods (e.g., `isUsernameRequiredErrorDisplayed()`): Check for the presence of specific error messages. **You will need to find the correct locators for your error messages.**
        *   `getCurrentUrl()`: Useful for verifying successful redirects.
        *   `isOnLoginPage()`: Checks if the current page appears to be the login page.
        *   Methods for other UI elements like "Forgot Password," "Sign Up," "Remember Me."
        *   `getPasswordFieldType()`: Verifies the `type` attribute of the password field.

3.  **`LoginTest.java` (Test Cases):**
    *   **JUnit 5:** Uses JUnit 5 for test execution (`@Test`, `@BeforeAll`, `@BeforeEach`, `@AfterEach`, `@AfterAll`, `@Order`, `@DisplayName`).
    *   **`@Order`:** Ensures tests run in a logical sequence, which is important for scenarios like "Multiple Failed Login Attempts."
    *   **`@DisplayName`:** Provides human-readable names for your tests in reports.
    *   **`BASE_URL`, `VALID_USERNAME`, `VALID_PASSWORD`:** **You MUST replace these placeholders with your actual application's details.**
    *   **Test Methods:** Each `@Test` method represents a specific test case derived from your review.
        *   They instantiate `LoginPage` to interact with the page.
        *   They use `Assertions` (`assertTrue`, `assertFalse`, `assertEquals`) to verify expected outcomes.
        *   They cover all the scenarios from your review:
            *   **Original:** TC\_LOGIN\_001 to TC\_LOGIN\_005.
            *   **Missing Scenarios:** Empty username, case sensitivity, "Remember Me," "Forgot Password," "Sign Up," multiple failures, special characters.
            *   **Negative Cases:** Exceeding length, leading/trailing spaces, control characters, XSS attempts.
            *   **Edge Cases:** Max length input, special characters only.
            *   **Security Cases:** SQL injection, HTTPS, password masking, session management, error message granularity.

**Before Running:**

1.  **Install Dependencies:**
    *   Add JUnit 5 Jupiter API to your `pom.xml` (if using Maven) or `build.gradle` (if using Gradle).
    *   Add Selenium WebDriver to your dependencies.
    *   Add `io.github.bonigarcia:webdrivermanager` for easy driver setup.

    **Maven Example (`pom.xml`):**
    ```xml
    <dependencies>
        <!-- JUnit 5 -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.10.0</version> <!-- Use the latest stable version -->
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
        <!-- Selenium WebDriver -->
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>4.14.1</version> <!-- Use the latest stable version -->
        </dependency>
        <!-- WebDriverManager -->
        <dependency>
            <groupId>io.github.bonigarcia</groupId>
            <artifactId>webdrivermanager</artifactId>
            <version>5.6.2</version> <!-- Use the latest stable version -->
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version> <!-- Or a compatible version -->
                <configuration>
                    <source>11</source> <!-- Or your Java version -->
                    <target>11</target> <!-- Or your Java version -->
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.1.2</version> <!-- Or a compatible version -->
            </plugin>
        </plugins>
    </build>
    ```

2.  **Configure Locators and URLs:**
    *   **CRITICAL:** Update `LoginPage.java` with the correct locators for your login page elements (username field, password field, login button, error messages).
    *   **CRITICAL:** Update `LoginTest.java` with your `BASE_URL`, `VALID_USERNAME`, and `VALID_PASSWORD`.
    *   Adjust assertion messages or expected results based on your application's specific behavior and error messages.

3.  **Run Tests:**
    *   You can run these tests from your IDE (e.g., by right-clicking `LoginTest.java` and selecting "Run JUnit Test").
    *   You can also run them from the command line using Maven (`mvn test`) or Gradle.

This comprehensive set of tests and the POM structure will provide a solid foundation for automating your login page testing. Remember that automation is an iterative process; you'll likely need to refine locators and assertions as your application evolves.