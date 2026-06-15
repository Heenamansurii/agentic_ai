def create_planner_prompt(requirement):

    prompt = f"""
Analyze this requirement and create a testing strategy.

Requirement:
{requirement}

Provide:
1. Test Scope
2. Test Types
3. Risks
4. Recommended Automation Areas
"""

    return prompt