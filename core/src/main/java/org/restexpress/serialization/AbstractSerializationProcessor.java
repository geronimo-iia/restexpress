/**
 *        Licensed to the Apache Software Foundation (ASF) under one
 *        or more contributor license agreements.  See the NOTICE file
 *        distributed with this work for additional information
 *        regarding copyright ownership.  The ASF licenses this file
 *        to you under the Apache License, Version 2.0 (the
 *        "License"); you may not use this file except in compliance
 *        with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *        Unless required by applicable law or agreed to in writing,
 *        software distributed under the License is distributed on an
 *        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *        KIND, either express or implied.  See the License for the
 *        specific language governing permissions and limitations
 *        under the License.
 *
 */
/*
 Copyright 2013, Strategic Gains, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.restexpress.serialization;

import java.util.List;
import java.util.Set;

import org.restexpress.contenttype.MediaRange;
import org.restexpress.contenttype.MediaTypeParser;

/**
 * {@link AbstractSerializationProcessor} implement common functionalities of
 * {@link SerializationProcessor}. TODO transform to immutable way
 * 
 * @author toddf
 * @since Jul 18, 2013
 */
public abstract class AbstractSerializationProcessor implements SerializationProcessor {
	/**
	 * supported format.
	 */
	private final String supportedFormat;
	/**
	 * {@link Set} of {@link MediaRange}.
	 */
	private final List<MediaRange> supportedMediaRanges;

	/**
	 * Build a new instance of AbstractSerializationProcessor.
	 * 
	 * @param supportedFormat
	 *            supported format
	 * @param supportedMediaRanges
	 *            {@link List} of supported {@link MediaRange}
	 */
	public AbstractSerializationProcessor(final String supportedFormat, final List<MediaRange> supportedMediaRanges) {
		super();
		this.supportedFormat = supportedFormat;
		this.supportedMediaRanges = supportedMediaRanges;
		// this.supportedMediaRanges = new ArrayList<MediaRange>(
		// supportedMediaRanges);
	}

	@Override
	public String getSupportedFormat() {
		return supportedFormat;
	}

	@Override
	public List<MediaRange> getSupportedMediaRanges() {
		return supportedMediaRanges;
	}

	/**
	 * Parse a media type string and add all the parse results to the supported
	 * media types of this SerializationProcessor.
	 * 
	 * @param mediaTypes
	 *            a Media Types string, containing possibly more-than one media
	 *            type
	 */
	public void addSupportedMediaTypes(final String mediaTypes) {
		addSupportedMediaRanges(MediaTypeParser.parse(mediaTypes));
	}

	public void addSupportedMediaRange(final MediaRange mediaRange) {
		if (!supportedMediaRanges.contains(mediaRange)) {
			supportedMediaRanges.add(mediaRange);
		}
	}

	public void addSupportedMediaRanges(final List<MediaRange> mediaRanges) {
		for (final MediaRange mediaRange : mediaRanges) {
			addSupportedMediaRange(mediaRange);
		}
	}

	public void setSupportedMediaRanges(final List<MediaRange> mediaRanges) {
		supportedMediaRanges.clear();
		supportedMediaRanges.addAll(mediaRanges);
	}
}
