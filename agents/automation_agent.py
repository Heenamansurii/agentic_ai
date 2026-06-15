def create_automation_prompt(requirement):

    with open(
        "prompts/automation_prompt.txt",
        "r",
        encoding="utf-8"
    ) as file:
        prompt_template = file.read()

    prompt = prompt_template.format(
        requirement=requirement
    )

    return prompt