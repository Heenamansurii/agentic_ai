def create_automation_prompt(
    requirement,
    review_output
):

    prompt = f"""
Requirement:

{requirement}

Review Feedback:

{review_output}

Generate Selenium Java automation code.

Cover all important scenarios mentioned in the review.
"""

    return prompt