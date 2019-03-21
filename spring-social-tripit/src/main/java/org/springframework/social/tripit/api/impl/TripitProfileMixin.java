/*
 * Copyright 2013 the original author or authors.
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
package org.springframework.social.tripit.api.impl;

import java.io.IOException;
import java.util.Iterator;

import org.springframework.social.tripit.api.TripItProfile;
import org.springframework.social.tripit.api.impl.TripitProfileMixin.TripItProfileDeserializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = TripItProfileDeserializer.class)
class TripitProfileMixin {

	static class TripItProfileDeserializer extends JsonDeserializer<TripItProfile> {

		@Override
		public TripItProfile deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			JsonNode tree = jp.readValueAsTree();
			JsonNode profileNode = tree.get("Profile");
			String id = profileNode.get("@attributes").get("ref").asText();
			String screenName = profileNode.get("screen_name").asText();
			String publicDisplayName = profileNode.get("public_display_name").asText();
			String emailAddress = getEmailAddress(profileNode);
			String homeCity = getTextNodeValue(profileNode, "home_city");
			String company = getTextNodeValue(profileNode, "company");
			String profilePath = profileNode.get("profile_url").asText();
			String profileImageUrl = getTextNodeValue(profileNode, "photo_url");
			
			return new TripItProfile(id, screenName, publicDisplayName, emailAddress, homeCity, company, profilePath, profileImageUrl);
		}

		private String getTextNodeValue(JsonNode parentNode, String nodeName) {
			return parentNode.has(nodeName) ? parentNode.get(nodeName).asText() : null;
		}
		
		private String getEmailAddress(JsonNode profileNode) {
			JsonNode emailsNode = profileNode.get("ProfileEmailAddresses").get("ProfileEmailAddress");
			if(emailsNode.asToken() == JsonToken.START_OBJECT) {
				return emailsNode.get("address").asText();
			} else if (emailsNode.asToken() == JsonToken.START_ARRAY) {
				for(Iterator<JsonNode> iterator = emailsNode.elements(); iterator.hasNext();) {
					JsonNode emailNode = iterator.next();
					if(emailNode.get("is_primary").asBoolean()) {
						return emailNode.get("address").asText();
					}
				}
			}
			return null;
		}
	}
}
