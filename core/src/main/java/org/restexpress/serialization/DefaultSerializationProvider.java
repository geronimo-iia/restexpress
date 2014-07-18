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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.intelligentsia.commons.http.exception.BadRequestException;
import org.intelligentsia.commons.http.exception.NotAcceptableException;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.common.exception.ConfigurationException;
import org.restexpress.common.util.StringUtils;
import org.restexpress.contenttype.MediaRange;
import org.restexpress.contenttype.MediaTypeParser;
import org.restexpress.response.ResponseProcessor;
import org.restexpress.response.ResponseWrapper;
import org.restexpress.response.Wrapper;
import org.restexpress.response.Wrapper.RawResponseWrapper;
import org.restexpress.serialization.json.JacksonJsonProcessor;
import org.restexpress.serialization.xml.XstreamXmlProcessor;

/**
 * {@link DefaultSerializationProvider} implements a
 * {@link SerializationProvider}.
 * 
 * By default, this instance use {@link JacksonJsonProcessor} for json as
 * default format, XstreamXmlProcessor for XML, and a {@link RawResponseWrapper}
 * 
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Jul 18, 2013
 */
public class DefaultSerializationProvider implements SerializationProvider {

	private final Map<String, ResponseProcessor> processorsByFormat = new HashMap<String, ResponseProcessor>();
	private final Map<String, ResponseProcessor> processorsByMediaType = new HashMap<String, ResponseProcessor>();
	private final List<MediaRange> supportedMediaRanges = new ArrayList<MediaRange>();
	private ResponseProcessor defaultProcessor;

	/**
	 * Build a new instance of {@link DefaultSerializationProvider} with
	 * {@link JacksonJsonProcessor} for json as default format,
	 * XstreamXmlProcessor for XML, and a {@link RawResponseWrapper}.
	 */
	public DefaultSerializationProvider() {
		this(true);
	}

	/**
	 * Build a new instance of {@link DefaultSerializationProvider}.
	 * 
	 * @param addDefault
	 *            if true, then use {@link JacksonJsonProcessor} for json as
	 *            default format, XstreamXmlProcessor for XML, and a
	 *            {@link RawResponseWrapper}.
	 */
	public DefaultSerializationProvider(final boolean addDefault) {
		if (addDefault) {
			add(new JacksonJsonProcessor(), Wrapper.newRawResponseWrapper(), true);
			add(new XstreamXmlProcessor(), Wrapper.newRawResponseWrapper());
		}
	}

	@Override
	public void add(final SerializationProcessor processor, final ResponseWrapper wrapper) {
		add(processor, wrapper, false);
	}

	@Override
	public void add(final SerializationProcessor processor, final ResponseWrapper wrapper, final boolean isDefault) {
		addMediaRanges(processor.getSupportedMediaRanges());
		final ResponseProcessor responseProcessor = new ResponseProcessor(processor, wrapper);

		for (final String format : processor.getSupportedFormats()) {
			if (processorsByFormat.containsKey(format)) {
				throw new ConfigurationException("Duplicate supported format: " + format);
			}

			processorsByFormat.put(format, responseProcessor);
		}

		for (final MediaRange mediaRange : processor.getSupportedMediaRanges()) {
			final String mediaType = mediaRange.asMediaType();

			if (!processorsByMediaType.containsKey(mediaType)) {
				processorsByMediaType.put(mediaRange.asMediaType(), responseProcessor);
			}
		}

		if (isDefault) {
			defaultProcessor = responseProcessor;
		}
	}

	@Override
	public void setDefaultFormat(final String format) {
		final ResponseProcessor processor = processorsByFormat.get(format);

		if (processor == null) {
			throw new RuntimeException("No serialization processor found for requested response format: " + format);
		}

		defaultProcessor = processor;
	}

	/**
	 * Provided for testing so that UTs can specify and format and compare the
	 * resolver-based results.
	 * 
	 * @param format
	 * @return
	 */
	public SerializationProcessor getSerializer(final String format) {
		final ResponseProcessor p = processorsByFormat.get(format);

		if (p != null) {
			return p.getSerializer();
		}

		return null;
	}

	@Override
	public SerializationSettings resolveRequest(final Request request) {
		ResponseProcessor processor = null;
		final String format = request.getFormat();

		if (format != null) {
			processor = processorsByFormat.get(format);

			if (processor == null) {
				throw new NotAcceptableException(format);
			}
		}

		if (processor == null) {
			final List<MediaRange> requestedMediaRanges = MediaTypeParser.parse(request.getHeader(HttpHeaders.Names.CONTENT_TYPE));
			final String bestMatch = MediaTypeParser.getBestMatch(supportedMediaRanges, requestedMediaRanges);

			if (bestMatch != null) {
				processor = processorsByMediaType.get(bestMatch);
			}
		}

		if (processor == null) {
			processor = defaultProcessor;
		}

		return new SerializationSettings(request.getHeader(HttpHeaders.Names.CONTENT_TYPE), processor);
	}

	@Override
	public SerializationSettings resolveResponse(final Request request, final Response response, final boolean shouldForce) {
		String bestMatch = null;
		ResponseProcessor processor = null;
		String format = request.getFormat();

		if (exceptionOccurredBeforeRouteResolution(format, response)) {
			format = parseFormatFromUrl(request.getUrl());
		}

		if (format != null) {
			processor = processorsByFormat.get(format);

			if (processor != null) {
				bestMatch = processor.getSupportedMediaRanges().get(0).asMediaType();
			} else if (!shouldForce) {
				throw new BadRequestException("Requested representation format not supported: " + format + ". Supported formats: " + StringUtils.join(", ", processorsByFormat.keySet()));
			}
		}

		if (processor == null) {
			final List<MediaRange> requestedMediaRanges = MediaTypeParser.parse(request.getHeader(HttpHeaders.Names.ACCEPT));
			bestMatch = MediaTypeParser.getBestMatch(supportedMediaRanges, requestedMediaRanges);

			if (bestMatch != null) {
				processor = processorsByMediaType.get(bestMatch);
			} else if (!shouldForce && !requestedMediaRanges.isEmpty()) {
				throw new NotAcceptableException("Supported Media Types: " + StringUtils.join(", ", supportedMediaRanges));
			}
		}

		if (processor == null) {
			processor = defaultProcessor;
			bestMatch = processor.getSupportedMediaRanges().get(0).asMediaType();
		}

		return new SerializationSettings(bestMatch, processor);
	}

	@Override
	public void alias(final String name, final Class<?> type) {
		for (final ResponseProcessor processor : processorsByFormat.values()) {
			if (Aliasable.class.isAssignableFrom(processor.getSerializer().getClass())) {
				((Aliasable) processor.getSerializer()).alias(name, type);
			}
		}
	}

	// SECTION: CONVENIENCE/SUPPORT

	private void addMediaRanges(final List<MediaRange> mediaRanges) {
		if (mediaRanges == null) {
			return;
		}

		for (final MediaRange mediaRange : mediaRanges) {
			if (!supportedMediaRanges.contains(mediaRange)) {
				supportedMediaRanges.add(mediaRange);
			}
		}
	}

	private boolean exceptionOccurredBeforeRouteResolution(final String format, final Response response) {
		return (format == null) && response.hasException();
	}

	private String parseFormatFromUrl(final String url) {
		final int queryDelimiterIndex = url.indexOf('?');
		final String path = (queryDelimiterIndex > 0 ? url.substring(0, queryDelimiterIndex) : url);
		final int formatDelimiterIndex = path.lastIndexOf('.');
		return (formatDelimiterIndex > 0 ? path.substring(formatDelimiterIndex + 1) : null);
	}

}
