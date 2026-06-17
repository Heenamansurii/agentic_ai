def create_playwright_prompt(requirement, testcases):


    prompt = f"""


    Generate Playwright Python automation script using pytest.

    Requirement:
    {requirement}

    Test Cases:
    {testcases}

    Rules:

      * Use Playwright Python
      * Use pytest format
      * Generate complete executable code
      * Include assertions
      * Return only code
       """

    return prompt
