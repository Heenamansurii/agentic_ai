import streamlit as st
from agents.planner_agent import create_planner_prompt
from agents.testcase_agent import create_testcase_prompt
from agents.review_agent import create_review_prompt
from agents.automation_agent import create_automation_prompt
from google import genai
import os
from dotenv import load_dotenv

load_dotenv()

client = genai.Client(api_key=os.getenv("GEMINI_API_KEY"))

st.title("AI Test Case Generator 🚀")

requirement = st.text_area("Enter Requirement")

if st.button("Generate"):

    planner_prompt = create_planner_prompt(requirement)

    planner_response = client.models.generate_content(
       model="gemini-2.5-flash",
       contents=planner_prompt
)

    st.subheader("Planner Output")
    st.write(planner_response.text)

    testcase_prompt = create_testcase_prompt(
       requirement,
       planner_response.text,
       5
)

    response = client.models.generate_content(
       model="gemini-2.5-flash",
       contents=testcase_prompt
)

    st.subheader("Test Cases")
    st.write(response.text)

    review_prompt = create_review_prompt(response.text)

    review_response = client.models.generate_content(
        model="gemini-2.5-flash",
        contents=review_prompt
)

    st.subheader("Review")
    st.write(review_response.text)

    automation_prompt = create_automation_prompt(
      requirement,
      review_response.text
)

    automation_response = client.models.generate_content(
       model="gemini-2.5-flash",
       contents=automation_prompt
)

    st.subheader("Automation Code")
    st.code(automation_response.text)

