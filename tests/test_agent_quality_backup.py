import os
import json
import pytest
from dotenv import load_dotenv
from deepeval.metrics import AnswerRelevancyMetric
from deepeval.test_case import LLMTestCase

load_dotenv()

def test_generated_json_quality():
    # 1. Asli requirement ko read karein (Input)
    requirement_path = "requirement.txt"
    assert os.path.exists(requirement_path), "requirement.txt file nahi mili!"
    
    with open(requirement_path, "r", encoding="utf-8") as file:
        original_requirement = file.read()

    # 2. Aapke agent ka banaya hua JSON output read karein (Actual Output)
    output_path = "outputs/json_testcases.txt"
    assert os.path.exists(output_path), "Agent ka output file nahi mila! Pehle main.py chalayein."
    
    with open(output_path, "r", encoding="utf-8") as file:
        agent_output = file.read()

    # 3. DeepEval ke liye Test Case ready karein
    test_case = LLMTestCase(
        input=original_requirement,
        actual_output=agent_output
    )

    # 4. Metric set karein (Threshold 0.7 matlab 70% accuracy jaroori hai)
    # Yeh background me aapki GEMINI_API_KEY ya OPENAI_API_KEY ko judge banayega
    relevancy_metric = AnswerRelevancyMetric(threshold=0.7)

    # 5. Run evaluation
    relevancy_metric.measure(test_case)
    
    print(f"\n📊 AI AGENT QUALITY SCORE: {relevancy_metric.score}")
    print(f"💡 REASONING BY JUDGE: {relevancy_metric.reason}")

    # 6. Final Assert (Pass/Fail)
    assert relevancy_metric.is_successful(), f"Test failed! Agent output quality is low: {relevancy_metric.score}"
