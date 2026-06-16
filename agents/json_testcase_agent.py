def create_json_testcase_prompt(requirement):


  prompt = f"""
Generate test cases in valid JSON format.

Requirement:

{requirement}

Return ONLY JSON.

Format:

[
{{
"testcase_id": "TC001",
"scenario": "Scenario Name",
"steps": ["step1", "step2"],
"expected_result": "Expected Result"
}}
]
"""


  return prompt

