from playwright.sync_api import sync_playwright

def test_valid_login():


    with sync_playwright() as p:

            browser = p.chromium.launch(headless=False)

            page = browser.new_page()

            page.goto("https://the-internet.herokuapp.com/login")

            page.fill("#username", "tomsmith")
            page.fill("#password", "SuperSecretPassword!")

            page.click("button[type='submit']")

            success_text = page.locator(".flash.success").text_content()
def test_valid_login(page):


            page.goto(
              "https://the-internet.herokuapp.com/login")

            page.fill(
              "#username",
              "tomsmith")

            page.fill(
              "#password",
              "SuperSecretPassword!")

            page.click(
              "button[type='submit']")

            success_text = page.locator(
              ".flash.success" 
               ).text_content()

            assert "You logged into a secure area!" in success_text


           

            

