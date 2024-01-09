/*
 * Copyright 2023 the original author or authors.
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

package org.springframework.ai.prompt.messages;

import java.util.Map;

public class ToolMessage extends AbstractMessage {

	public ToolMessage(String content) {
		super(MessageType.TOOL, content);
	}

	public ToolMessage(String content, Map<String, Object> properties) {
		super(MessageType.TOOL, content, properties);
	}

	@Override
	public String toString() {
		return "ToolMessage{" + "content='" + content + '\'' + ", properties=" + properties + ", messageType="
				+ messageType + '}';
	}

}
