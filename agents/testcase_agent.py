def create_testcase_prompt(requirement,    planning_output,testcase_count):
    with open(
        "prompts/testcase_prompt.txt",
        "r",
        encoding="utf-8"
    ) as file:
        prompt_template = file.read()

    prompt = prompt_template.format(
        requirement=requirement,
        testcase_count=testcase_count
    )

    return prompt