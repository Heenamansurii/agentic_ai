from agents.testcase_agent import create_testcase_prompt
from agents.automation_agent import create_automation_prompt
from agents.review_agent import create_review_prompt
from agents.planner_agent import create_planner_prompt

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

print("Requirement Loaded:\n")
print(requirement)

# Planner Prompt

planner_prompt = create_planner_prompt(
    requirement
)

print("\nPlanner Prompt:\n")
print(planner_prompt)

# Decision Making

if len(requirement) < 100:
   testcase_count = 5
else:
   testcase_count = 20

print(f"\nGenerating {testcase_count} Test Cases")

# Prompts

final_prompt = create_testcase_prompt(
requirement,
testcase_count
)

automation_prompt = create_automation_prompt(
requirement
)

print("\nFinal Prompt:\n")
print(final_prompt)



    # PLANNER AGENT

try:
    planner_response = client.models.generate_content(
    model="gemini-3.5-flash",
    contents=planner_prompt
    )


    print("\nPlanning Output:\n")
    print(planner_response.text)

    with open("outputs/planning.txt", "w", encoding="utf-8") as file:
        file.write(planner_response.text)

    print("\nPlanning saved in outputs/planning.txt")


except Exception as e:
     print("\nPlanner Error:")
     print(e)


# TEST CASE GENERATION + REVIEW

try:
    print("\nCalling Gemini API...")


    response = client.models.generate_content(
    model="gemini-3.5-flash",
    contents=final_prompt  
   )

    print("\nGemini API Response Received")

    print("\nAI Response:\n")
    print(response.text)

# Save Test Cases
    with open("outputs/testcases.txt", "w", encoding="utf-8") as file:
       file.write(response.text)

    print("\nTest Cases saved in outputs/testcases.txt")

# Review Prompt
    review_prompt = create_review_prompt(
      response.text
)

    print("\nReview Prompt:\n")
    print(review_prompt)

# Review Agent
    review_response = client.models.generate_content(
    model="gemini-3.5-flash",
    contents=review_prompt
)

    print("\nReview Output:\n")
    print(review_response.text)

    with open("outputs/review.txt", "w", encoding="utf-8") as file:
       file.write(review_response.text)

    print("\nReview saved in outputs/review.txt")


except Exception as e:
    print("\nTest Case / Review Error:")
    print(e)

# AUTOMATION CODE GENERATION

try:
    automation_response = client.models.generate_content(
    model="gemini-3.5-flash",
    contents=automation_prompt
   )


    print("\nAutomation Code:\n")
    print(automation_response.text)

    with open("outputs/automation.java", "w", encoding="utf-8") as file:
       file.write(automation_response.text)

    print("\nAutomation code saved in outputs/automation.java")


except Exception as e:
    print("\nAutomation Error:")
    print(e)

   
