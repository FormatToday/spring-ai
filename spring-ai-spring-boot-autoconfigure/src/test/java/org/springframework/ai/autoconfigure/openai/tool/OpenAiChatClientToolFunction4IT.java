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

package org.springframework.ai.autoconfigure.openai.tool;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;
import org.springframework.ai.autoconfigure.openai.tool.FakeWeatherService.Response;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.model.AbstractToolFunctionCallback;
import org.springframework.ai.openai.client.OpenAiChatClient;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.messages.Message;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".*")
public class OpenAiChatClientToolFunction4IT {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withPropertyValues("spring.ai.openai.apiKey=" + System.getenv("OPENAI_API_KEY"))
		.withConfiguration(AutoConfigurations.of(OpenAiAutoConfiguration.class));

	@Test
	void functionCallTest() {
		contextRunner.withPropertyValues("spring.ai.openai.chat.options.model=gpt-4-1106-preview").run(context -> {

			OpenAiChatClient chatClient = context.getBean(OpenAiChatClient.class);

			UserMessage userMessage = new UserMessage("What's the weather like in San Francisco, Tokyo, and Paris?");

			List<Message> messages = new ArrayList<>(List.of(userMessage));

			ChatResponse response = chatClient.generateWithTools(new Prompt(messages).withToolCallback(
					new AbstractToolFunctionCallback<FakeWeatherService.Request, FakeWeatherService.Response>(
							"getCurrentWeather", "Get the weather in location", FakeWeatherService.Request.class) {

						private final FakeWeatherService weatherService = new FakeWeatherService();

						@Override
						public FakeWeatherService.Response doCall(FakeWeatherService.Request request) {
							return weatherService.apply(request);
						}

						@Override
						public String doResponseToString(Response response) {
							return "" + response.temp() + response.unit();
						}

					}));

			System.out.println(response.getGeneration().getContent());

			assertThat(response.getGeneration().getContent()).contains("30", "10", "15");

		});
	}

}
