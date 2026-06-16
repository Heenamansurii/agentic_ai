from agents.testcase_agent import create_testcase_prompt
from agents.automation_agent import create_automation_prompt
from agents.review_agent import create_review_prompt
from agents.planner_agent import create_planner_prompt
from agents.json_testcase_agent import create_json_testcase_prompt
from utils.excel_exporter import export_testcases_to_excel

from google import genai
from dotenv import load_dotenv
import os
import json


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





    # PLANNER AGENT

try:
    planner_response = client.models.generate_content(
    model="gemini-2.5-flash",
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
     exit()

     
    # Create Final Prompt using Planner Output

final_prompt = create_testcase_prompt(
    requirement,
    planner_response.text,
    testcase_count
)

print("\nFinal Prompt:\n")
print(final_prompt)




# TEST CASE GENERATION + REVIEW

try:
    print("\nCalling Gemini API...")


    response = client.models.generate_content(
    model="gemini-2.5-flash-lite",
    contents=final_prompt  
   )

    print("\nGemini API Response Received")

    print("\nAI Response:\n")
    print(response.text)

    with open("outputs/testcases.txt", "w", encoding="utf-8") as file: file.write(response.text) 
    print("\nTest Cases saved in outputs/testcases.txt")



# Review Prompt
    review_prompt = create_review_prompt(
      response.text
)

    print("\nReview Prompt:\n")
    print(review_prompt)

# Review Agent
    review_response = client.models.generate_content(
    model="gemini-2.5-flash-lite",
    contents=review_prompt
)

    print("\nReview Output:\n")
    print(review_response.text)

     #save
    with open("outputs/review.txt", "w", encoding="utf-8") as file:
       file.write(review_response.text)

    print("\nReview saved in outputs/review.txt")

    
      #automtion
    automation_prompt = create_automation_prompt(
    requirement,
    review_response.text
)



except Exception as e:
    print("\nTest Case / Review Error:")
    print(e)

# AUTOMATION CODE GENERATION
if 'automation_prompt' in locals():
    try:
        automation_response = client.models.generate_content(
        model="gemini-2.5-flash-lite",
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
else:
        print("\nAutomation skipped because previous step failed.")

# ---------------------------

# JSON TEST CASE AGENT

# ---------------------------

try:


    json_prompt = create_json_testcase_prompt(requirement)

    json_response = client.models.generate_content(
    model="gemini-2.5-flash-lite",
    contents=json_prompt
)
 
    print("\nJSON Test Cases:\n")
    print(json_response.text)

    with open("outputs/json_testcases.txt","w",encoding="utf-8") as file:
       file.write(json_response.text)

    print("\nJSON Test Cases saved in outputs/json_testcases.txt")

# ---------------------------
# EXCEL EXPORT (NESTED TRY)
# ---------------------------
    try:
        print("\nConverting JSON to Excel...")

        raw_json = json_response.text.strip()

    # Remove markdown ```json
        if "```" in raw_json:
             raw_json = raw_json.replace("```json", "")
             raw_json = raw_json.replace("```", "")

        testcases = json.loads(raw_json)

        export_testcases_to_excel(testcases)

        print("\nExcel file created: outputs/testcases.xlsx")

    except Exception as e:
         print("\nExcel Export Error:")
         print(e)

         print("\nRAW OUTPUT WAS:\n")   
         print(json_response.text)