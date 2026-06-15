from google import genai
from dotenv import load_dotenv
import os

load_dotenv()

client = genai.Client(
    api_key=os.getenv("GEMINI_API_KEY")
)

# Requirement Read
with open("requirement.txt", "r", encoding="utf-8") as file:
    requirement = file.read()

print("\nLength of Requirement:")
print(len(requirement))

# Test Case Prompt
testcase_prompt = f"""
Generate detailed test cases for:

{requirement}
"""

# Automation Prompt
automation_prompt = f"""
Generate Selenium Java automation script for:

{requirement}
"""

print("Requirement Loaded:\n")
print(requirement)

# Decision Making
if len(requirement) < 100:
    testcase_count = 5
else:
    testcase_count = 20

print(f"\nGenerating {testcase_count} Test Cases")

# Final Prompt
final_prompt = f"""
Generate {testcase_count} detailed functional test cases for:

{requirement}
"""

print("\nFinal Prompt:\n")
print(final_prompt)

# TEST CASE GENERATION
try:
    response = client.models.generate_content(
        model="gemini-3.5-flash",
        contents=final_prompt
    )

    print("\nAI Response:\n")
    print(response.text)

    with open("testcases.txt", "w", encoding="utf-8") as file:
        file.write(response.text)

    print("\nTest Cases saved in testcases.txt")

except Exception as e:
    print("\nTest Case Error:")
    print(e)

# AUTOMATION CODE GENERATION
try:
    automation_response = client.models.generate_content(
        model="gemini-3.5-flash",
        contents=automation_prompt
    )

    print("\nAutomation Code:\n")
    print(automation_response.text)

    with open("automation.java", "w", encoding="utf-8") as file:
        file.write(automation_response.text)

    print("\nAutomation code saved in automation.java")

except Exception as e:
    print("\nAutomation Error:")
    print(e)