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
package org.restexpress.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link MediaRanges} is useful for parsing Content-Type or Accept HTTP
 * headers to both parse them into an order list of MediaRange preferences and
 * for matching supported MediaRanges to requested ones for serialization.
 * 
 * @author toddf
 * @since Jan 18, 2013
 */
public enum MediaRanges {
	;

	/**
	 * Parses a Content-Type or Accept header into an ordered List of MediaRange
	 * instances, which in turn can be used to determine which media type is
	 * most appropriate for serialization.
	 * 
	 * @param mediaType
	 * @return a List of MediaRange instances parsed from the mediaType string.
	 */
	public static List<MediaRange> parse(final String mediaType) {
		if (mediaType == null) {
			return Collections.emptyList();
		}

		final String[] segments = mediaType.split("\\s*,\\s*");
		final List<MediaRange> items = new ArrayList<MediaRange>();

		for (final String segment : segments) {
			items.add(MediaRange.parse(segment));
		}

		return items;
	}

	/**
	 * Given a List of supported MediaRanges and requested MediaRanges, returns
	 * the single best match in Content-Type header format.
	 * 
	 * @param supportedRanges
	 *            an ordered List of supported MediaRanges.
	 * @param requestedRanges
	 *            an ordered List of MediaRanges that the client desires.
	 * @return the single best MediaRange match in Content-Type header format
	 *         (String). Or null if no match found.
	 */
	public static String getBestMatch(final List<MediaRange> supportedRanges, final List<MediaRange> requestedRanges) {
		final List<WeightedMatch> matches = new ArrayList<WeightedMatch>();

		for (final MediaRange supportedRange : supportedRanges) {
			final WeightedMatch m = getWeightedMatch(supportedRange, requestedRanges);

			if (m != null) {
				matches.add(m);
			}
		}

		if (matches.isEmpty()) {
			return null;
		}
		if (matches.size() == 1) {
			return matches.get(0).mediaRange.asMediaType();
		}

		Collections.sort(matches);
		return matches.get(0).mediaRange.asMediaType();
	}

	/**
	 * Iterates the requested MediaRanges to determine how well the single
	 * supported MediaRange matches.
	 * 
	 * @param supportedRange
	 * @param requestedRanges
	 * @return a WeightedMatch
	 */
	private static WeightedMatch getWeightedMatch(final MediaRange supportedRange, final List<MediaRange> requestedRanges) {
		int maxRank = -1;
		MediaRange bestMatch = null;

		for (final MediaRange requestedRange : requestedRanges) {
			final int rank = supportedRange.rankAgainst(requestedRange);

			if (rank > maxRank) {
				maxRank = rank;
				bestMatch = supportedRange;
			}
		}

		return (maxRank == -1 ? null : new WeightedMatch(bestMatch, maxRank));
	}

	/**
	 * WeightedMatch.
	 * 
	 */
	protected static class WeightedMatch implements Comparable<WeightedMatch> {
		protected final MediaRange mediaRange;
		protected final int rank;

		public WeightedMatch(final MediaRange range, final int rank) {
			this.mediaRange = range;
			this.rank = rank;
		}

		/**
		 * Reverse-rank natural ordering.
		 */
		@Override
		public int compareTo(final WeightedMatch that) {
			final int rankSign = (that.rank - this.rank);
			if (rankSign == 0) {
				return (int) ((that.mediaRange.qvalue - this.mediaRange.qvalue) * 10);
			}
			return rankSign;
		}
	}
}
