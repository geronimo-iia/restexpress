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
package org.restexpress.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link MediaRange} is a single segment parsed from an Accept or
 * Content-Type header.
 * 
 * @author toddf
 * @since Jan 18, 2013
 */
public class MediaRange {
	private static final String MEDIA_TYPE_REGEX = "(\\S+?|\\*)/(\\S+?|\\*)";
	private static final Pattern MEDIA_TYPE_PATTERN = Pattern.compile(MEDIA_TYPE_REGEX);
	private static final String PARAMETER_REGEX = "(\\w+?)(?:\\s*?=\\s*?(\\S+?))";
	private static final Pattern PARAMETER_PATTERN = Pattern.compile(PARAMETER_REGEX);

	protected String name;
	protected String type;
	protected String subtype;
	protected float qvalue = 1.0f;
	protected Map<String, String> parameters = new HashMap<String, String>();

	/**
	 * Build a new instance of {@link MediaRange}.
	 * 
	 * @param value
	 */
	public MediaRange(final String value) {
		this.name = value;
	}

	@Override
	public String toString() {
		return name;
	}

	public String asMediaType() {
		final StringBuilder b = new StringBuilder(type);
		b.append("/");
		b.append(subtype);

		for (final Entry<String, String> entry : parameters.entrySet()) {
			b.append("; ");
			b.append(entry.getKey());

			if (entry.getValue() != null) {
				b.append("=");
				b.append(entry.getValue());
			}
		}

		return b.toString();
	}

	/**
	 * 
	 * @param that
	 * @return -1 if not applicable. Otherwise a rank value >= 0
	 */
	public int rankAgainst(final MediaRange that) {
		int rank = -1; // Default is not-applicable.

		if ((this.type.equals(that.type) || "*".equals(this.type) || "*".equals(that.subtype)) && (this.subtype.equals(that.subtype) || "*".equals(that.subtype) || "*".equals(this.subtype))) {
			rank = 0; // This media type IS applicable

			if (this.type.equals(that.type)) {
				rank += 100;
			}

			if (this.subtype.equals(that.subtype) && !"*".equals(this.subtype)) {
				rank += 50;
			}

			for (final Entry<String, String> entry : parameters.entrySet()) {
				final String value = that.parameters.get(entry.getKey());

				if ((value != null) && value.equals(entry.getValue())) {
					rank += 2;
				}
			}
		}

		return rank;
	}

	@Override
	public boolean equals(final Object that) {
		return equals((MediaRange) that);
	}

	public boolean equals(final MediaRange that) {
		final boolean result = (name.equals(that.name) && type.equals(that.type) && subtype.equals(that.subtype));

		if (!result) {
			return false;
		}

		if (qvalue != that.qvalue) {
			return false;
		}

		return parameters.equals(that.parameters);
	}

	@Override
	public int hashCode() {
		return 31 + name.hashCode() + parameters.hashCode() + (int) (qvalue * 10.0);
	}

	/**
	 * Parse segment.
	 * 
	 * @param segment
	 * @return {@link MediaRange} instance.
	 */
	public static MediaRange parse(final String segment) {
		final MediaRange r = new MediaRange(segment);
		final String[] pieces = segment.split("\\s*;\\s*");
		final Matcher x = MEDIA_TYPE_PATTERN.matcher(pieces[0]);
		if (x.matches()) {
			r.type = x.group(1);
			r.subtype = x.group(2);
		}
		for (int i = 1; i < pieces.length; ++i) {
			final Matcher p = PARAMETER_PATTERN.matcher(pieces[i]);

			if (p.matches()) {
				final String token = p.group(1);
				final String value = p.group(2);

				if ("q".equalsIgnoreCase(token)) {
					r.qvalue = Float.parseFloat(value);
				} else if (value != null) {
					r.parameters.put(token, value);
				} else {
					r.parameters.put(token, null);
				}
			}
		}
		return r;
	}
}
