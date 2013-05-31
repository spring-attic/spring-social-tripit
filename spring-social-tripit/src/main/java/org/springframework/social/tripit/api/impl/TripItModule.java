/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.tripit.api.impl;

import org.springframework.social.tripit.api.Trip;
import org.springframework.social.tripit.api.TripItProfile;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Jackson module for registering mixin annotations against TripIt model classes.
 */
public class TripItModule extends SimpleModule {
	
	public TripItModule() {
		super("TripItModule");
	}
	
	@Override
	public void setupModule(SetupContext context) {
		context.setMixInAnnotations(Trip.class, TripMixin.class);
		context.setMixInAnnotations(TripItProfile.class, TripitProfileMixin.class);
	}

}
