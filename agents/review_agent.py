def create_review_prompt(testcases):

    prompt = f"""
Review the following test cases.

Identify:
- Missing scenarios
- Negative test cases
- Edge cases
- Security test cases

Test Cases:

{testcases}
"""

    return prompt