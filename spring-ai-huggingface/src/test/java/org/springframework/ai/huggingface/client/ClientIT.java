/*
 * Copyright 2023-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.ai.huggingface.client;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import org.springframework.ai.client.AiResponse;
import org.springframework.ai.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Until a valid inference endpoint is available for the provided HUGGINGFACE_API_KEY ")
@SpringBootTest
@EnabledIfEnvironmentVariable(named = "HUGGINGFACE_API_KEY", matches = ".+")
public class ClientIT {

	@Autowired
	protected HuggingfaceAiClient huggingfaceAiClient;

	@Test
	void helloWorldCompletion() {
		String mistral7bInstruct = """
				[INST] You are a helpful code assistant. Your task is to generate a valid JSON object based on the given information:
				name: John
				lastname: Smith
				address: #1 Samuel St.
				Just generate the JSON object without explanations:
				[/INST]
				 """;
		Prompt prompt = new Prompt(mistral7bInstruct);
		AiResponse aiResponse = huggingfaceAiClient.generate(prompt);
		assertThat(aiResponse.getGeneration().getContent()).isNotEmpty();
		String expectedResponse = """
				```json
				{
				    "name": "John",
				    "lastname": "Smith",
				    "address": "#1 Samuel St."
				}
				```""";
		assertThat(aiResponse.getGeneration().getContent()).isEqualTo(expectedResponse);
		assertThat(aiResponse.getGeneration().getProperties()).containsKey("generated_tokens");
		assertThat(aiResponse.getGeneration().getProperties()).containsEntry("generated_tokens", 39);

	}

}
