
import pytest
from playwright.sync_api import Page, sync_playwright

# Define your base URL and credentials here
BASE_URL = "YOUR_LOGIN_PAGE_URL"  # Replace with your actual login page URL
VALID_USERNAME = "valid_user"  # Replace with a valid username
VALID_PASSWORD = "valid_password"  # Replace with a valid password
INVALID_USERNAME = "invalid_user"
INVALID_PASSWORD = "invalid_password"
USERNAME_WITH_SPECIAL_CHARS = "user@#$"
PASSWORD_WITH_SPECIAL_CHARS = "P@$$wOrd!"

# Locators for the login page elements
USERNAME_FIELD = lambda page: page.locator("#username")  # Replace with your actual username field locator
PASSWORD_FIELD = lambda page: page.locator("#password")  # Replace with your actual password field locator
LOGIN_BUTTON = lambda page: page.locator("button[type='submit']")  # Replace with your actual login button locator
DASHBOARD_PAGE_INDICATOR = lambda page: page.locator("#dashboard") # Replace with an element unique to your dashboard/home page
ERROR_MESSAGE = lambda page: page.locator(".error-message")  # Replace with your actual error message locator

# Helper function to perform login
def perform_login(page: Page, username, password):
    USERNAME_FIELD(page).fill(username)
    PASSWORD_FIELD(page).fill(password)
    LOGIN_BUTTON(page).click()

@pytest.fixture(scope="function")
def page():
    with sync_playwright() as p:
        browser = p.chromium.launch()
        page = browser.new_page()
        yield page
        browser.close()

def test_tc_login_001_successful_login(page: Page):
    """
    TC_LOGIN_001: Successful Login with Valid Credentials
    """
    page.goto(BASE_URL)
    perform_login(page, VALID_USERNAME, VALID_PASSWORD)
    # Assert that the user is redirected to the dashboard/home page
    assert DASHBOARD_PAGE_INDICATOR(page).is_visible()

def test_tc_login_002_invalid_username(page: Page):
    """
    TC_LOGIN_002: Login with Invalid Username and Valid Password
    """
    page.goto(BASE_URL)
    perform_login(page, INVALID_USERNAME, VALID_PASSWORD)
    # Assert that an error message is displayed
    assert ERROR_MESSAGE(page).inner_text() == "Invalid username or password" # Adjust message as needed

def test_tc_login_003_invalid_password(page: Page):
    """
    TC_LOGIN_003: Login with Valid Username and Invalid Password
    """
    page.goto(BASE_URL)
    perform_login(page, VALID_USERNAME, INVALID_PASSWORD)
    # Assert that an error message is displayed
    assert ERROR_MESSAGE(page).inner_text() == "Invalid username or password" # Adjust message as needed

def test_tc_login_004_empty_username(page: Page):
    """
    TC_LOGIN_004: Login with Empty Username and Valid Password
    """
    page.goto(BASE_URL)
    USERNAME_FIELD(page).fill("")
    PASSWORD_FIELD(page).fill(VALID_PASSWORD)
    LOGIN_BUTTON(page).click()
    # Assert that an error message is displayed for required username
    assert ERROR_MESSAGE(page).inner_text() == "Username is required" # Adjust message as needed

def test_tc_login_005_empty_password(page: Page):
    """
    TC_LOGIN_005: Login with Valid Username and Empty Password
    """
    page.goto(BASE_URL)
    USERNAME_FIELD(page).fill(VALID_USERNAME)
    PASSWORD_FIELD(page).fill("")
    LOGIN_BUTTON(page).click()
    # Assert that an error message is displayed for required password
    assert ERROR_MESSAGE(page).inner_text() == "Password is required" # Adjust message as needed

def test_tc_login_006_empty_username_and_password(page: Page):
    """
    TC_LOGIN_006: Login with Empty Username and Empty Password
    """
    page.goto(BASE_URL)
    USERNAME_FIELD(page).fill("")
    PASSWORD_FIELD(page).fill("")
    LOGIN_BUTTON(page).click()
    # Assert that error messages are displayed for both fields
    assert ERROR_MESSAGE(page).count() >= 2 # Assuming at least two error messages for clarity
    # More specific checks could be added if error messages are distinct and accessible

def test_tc_login_007_username_case_sensitivity(page: Page):
    """
    TC_LOGIN_007: Case Sensitivity of Username
    """
    page.goto(BASE_URL)
    # Assuming the system is case-insensitive for usernames for this test.
    # If it's case-sensitive, this test should assert for an error message.
    perform_login(page, VALID_USERNAME.upper(), VALID_PASSWORD)
    # Assert that login is successful (assuming case-insensitivity)
    assert DASHBOARD_PAGE_INDICATOR(page).is_visible()

    # To test case-sensitivity if the system IS case-sensitive:
    # page.goto(BASE_URL)
    # perform_login(page, VALID_USERNAME.upper(), VALID_PASSWORD)
    # assert ERROR_MESSAGE(page).inner_text() == "Invalid username or password" # Adjust message

def test_tc_login_008_password_case_sensitivity(page: Page):
    """
    TC_LOGIN_008: Case Sensitivity of Password
    """
    page.goto(BASE_URL)
    perform_login(page, VALID_USERNAME, INVALID_PASSWORD) # Using an invalid casing for password
    # Assert that an error message is displayed as passwords are typically case-sensitive
    assert ERROR_MESSAGE(page).inner_text() == "Invalid username or password" # Adjust message as needed

def test_tc_login_009_username_special_characters(page: Page):
    """
    TC_LOGIN_009: Attempt Login with Username containing Special Characters (if not allowed)
    """
    page.goto(BASE_URL)
    perform_login(page, USERNAME_WITH_SPECIAL_CHARS, VALID_PASSWORD)
    # Assert that an error message is displayed or login fails
    # This assertion might need to be more specific based on the actual error message
    assert ERROR_MESSAGE(page).is_visible()
    # Example for a specific error message:
    # assert ERROR_MESSAGE(page).inner_text() == "Username contains invalid characters"

def test_tc_login_010_password_special_characters(page: Page):
    """
    TC_LOGIN_010: Attempt Login with Password containing Special Characters (if allowed)
    """
    page.goto(BASE_URL)
    # This test assumes special characters are allowed in the password.
    # If they are not, the expected result would be an error message.
    perform_login(page, VALID_USERNAME, PASSWORD_WITH_SPECIAL_CHARS)
    # Assert successful login if special characters are allowed
    assert DASHBOARD_PAGE_INDICATOR(page).is_visible()

    # If special characters are NOT allowed in password, the test would look like this:
    # page.goto(BASE_URL)
    # perform_login(page, VALID_USERNAME, PASSWORD_WITH_SPECIAL_CHARS)
    # assert ERROR_MESSAGE(page).inner_text() == "Password contains invalid characters" # Adjust message

def test_tc_login_011_password_masking(page: Page):
    """
    TC_LOGIN_011: Verify Password Masking
    """
    page.goto(BASE_URL)
    test_password = "some_secret_password"
    PASSWORD_FIELD(page).fill(test_password)
    # Assert that the password field's type is 'password'
    assert PASSWORD_FIELD(page).get_attribute("type") == "password"

def test_tc_login_012_login_button_enabled_when_filled(page: Page):
    """
    TC_LOGIN_012: Login Button State - Enabled when fields are filled
    """
    page.goto(BASE_URL)
    USERNAME_FIELD(page).fill(VALID_USERNAME)
    PASSWORD_FIELD(page).fill(VALID_PASSWORD)
    # Assert that the login button is enabled
    assert LOGIN_BUTTON(page).is_enabled()

def test_tc_login_013_login_button_disabled_when_empty(page: Page):
    """
    TC_LOGIN_013: Login Button State - Disabled when fields are empty
    """
    page.goto(BASE_URL)
    # Assert that the login button is disabled by default
    assert LOGIN_BUTTON(page).is_disabled()
