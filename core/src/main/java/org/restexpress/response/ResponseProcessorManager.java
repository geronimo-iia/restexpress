/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership. The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 */
package org.restexpress.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.intelligentsia.commons.http.exception.BadRequestException;
import org.intelligentsia.commons.http.exception.NotAcceptableException;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.SerializationProvider;
import org.restexpress.common.StringUtils;
import org.restexpress.domain.Format;
import org.restexpress.serialization.Processor;

/**
 * ResponseProcessorManager manager {@link ResponseProcessor}. This class implements a {@link SerializationProvider} and a
 * {@link ResponseProcessorSettingResolver}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class ResponseProcessorManager implements ResponseProcessorSettingResolver, SerializationProvider {

    /**
     * {@link List} of {@link MediaRange}.
     */
    private final List<MediaRange> supportedMediaRanges = new ArrayList<>();

    /**
     * {@link Map} of {@link ResponseProcessor} per media type
     */
    private final Map<String, ResponseProcessor> processorsByMediaType = new HashMap<>();

    /**
     * Default {@link ResponseProcessor}.
     */
    private ResponseProcessor defaultResponseProcessor;

    /**
     * A {@link Map} of extension and list of {@link MediaRange} used to resolve format.
     */
    private final Map<String, List<MediaRange>> mediaTypePerFormat;

    /**
     * Build a new instance of ResponseProcessorManager.
     */
    public ResponseProcessorManager() {
        super();
        mediaTypePerFormat = new HashMap<>();
        // compute media range
        for (Entry<String, String> entry : Format.toMap().entrySet()) {
            mediaTypePerFormat.put(entry.getKey(), MediaRanges.parse(entry.getValue()));
        }
    }

    @Override
    public ResponseProcessorSetting resolve(final Request request) {
        ResponseProcessor responseProcessor = null;
        final String format = request.getFormat();
        if (format != null) {
            responseProcessor = getProcessorsByFormat(format);
            if (responseProcessor == null) {
                throw new BadRequestException(format);
            }
        }
        if (responseProcessor == null) {
            final List<MediaRange> requestedMediaRanges = MediaRanges.parse(request.getHeader(HttpHeaders.Names.CONTENT_TYPE));
            final String bestMatch = MediaRanges.getBestMatch(supportedMediaRanges, requestedMediaRanges);
            if (bestMatch != null) {
                responseProcessor = processorsByMediaType.get(bestMatch);
            }
        }
        if (responseProcessor == null) {
            responseProcessor = defaultResponseProcessor;
        }
        return new ResponseProcessorSetting(request.getHeader(HttpHeaders.Names.CONTENT_TYPE), responseProcessor);
    }

    @Override
    public ResponseProcessorSetting resolve(final Request request, final Response response, final boolean shouldForce) {
        String bestMatch = null;
        ResponseProcessor processor = null;
        String format = request.getFormat();
        // if we have no header format and exception, try to find format
        if ((format == null) && response.hasException()) {
            // try to obtain format from URL
            format = parseFormatFromUrl(request.getUrl());
        }
        // if format is set, lookup for associated ResponseProcessor
        if (format != null) {
            processor = getProcessorsByFormat(format);
            if (processor != null) {
                bestMatch = processor.processor().mediaType();
            } else if (!shouldForce) {
                throw new BadRequestException("Requested representation format not supported: " + format + ". Supported Media Types: "
                        + StringUtils.join(", ", supportedMediaRanges));
            }
        }
        if (processor == null) {
            final List<MediaRange> requestedMediaRanges = MediaRanges.parse(request.getHeader(HttpHeaders.Names.ACCEPT));
            bestMatch = MediaRanges.getBestMatch(supportedMediaRanges, requestedMediaRanges);
            if (bestMatch != null) {
                processor = processorsByMediaType.get(bestMatch);
            } else if (!shouldForce && !requestedMediaRanges.isEmpty()) {
                throw new NotAcceptableException("Supported Media Types: " + StringUtils.join(", ", supportedMediaRanges));
            }
        }
        if (processor == null) {
            processor = defaultResponseProcessor;
            bestMatch = processor.processor().mediaType();
        }
        return new ResponseProcessorSetting(bestMatch, processor);
    }

    @Override
    public SerializationProvider add(Processor processor, ResponseWrapper responseWrapper) {
        return add(processor, responseWrapper, false);
    }

    @Override
    public SerializationProvider add(Processor processor, ResponseWrapper responseWrapper, boolean isDefault) {
        ResponseProcessor responseProcessor = new ResponseProcessor(processor, responseWrapper);
        // compute Media Range
        List<MediaRange> supportedMediaRanges = MediaRanges.parse(processor.supportedMediaTypes());
        // add to this provider
        addMediaRanges(supportedMediaRanges);
        // register his supported media type
        for (final MediaRange mediaRange : supportedMediaRanges) {
            final String mediaType = mediaRange.asMediaType();
            if (!processorsByMediaType.containsKey(mediaType)) {
                processorsByMediaType.put(mediaRange.asMediaType(), responseProcessor);
            }
        }
        // register default
        if (isDefault || defaultResponseProcessor == null) {
            defaultResponseProcessor = responseProcessor;
        }
        return this;
    }

    /**
     * @param format
     * @return a {@link ResponseProcessor} instance or null if no {@link ResponseProcessor} is found for specified format parameter.
     */
    protected ResponseProcessor getProcessorsByFormat(String format) {
        if (format != null) {
            List<MediaRange> requested = mediaTypePerFormat.get(format);
            if (requested != null) {
                final String bestMatch = MediaRanges.getBestMatch(supportedMediaRanges, requested);
                if (bestMatch != null) {
                    return processorsByMediaType.get(bestMatch);
                }
            }
        }
        return null;
    }

    public Set<String> supportedMediaType() {
        return processorsByMediaType.keySet();
    }

    @Override
    public Processor processor(String mimeType) {
        List<MediaRange> requested = MediaRanges.parse(mimeType);
        final String bestMatch = MediaRanges.getBestMatch(supportedMediaRanges, requested);
        if (bestMatch != null) {
            ResponseProcessor responseProcessor = processorsByMediaType.get(bestMatch);
            return responseProcessor != null ? responseProcessor.processor() : null;
        }
        return null;
    }

    @Override
    public Processor defaultProcessor() {
        return defaultResponseProcessor.processor();
    }

    /**
     * Add specified {@link MediaRange} if they're not ever exists. TODO add test case
     * 
     * @param mediaRanges
     */
    protected void addMediaRanges(final List<MediaRange> mediaRanges) {
        if (mediaRanges != null) {
            for (MediaRange mediaRange : mediaRanges) {
                if (!supportedMediaRanges.contains(mediaRange))
                    supportedMediaRanges.add(mediaRange);
            }
        }
    }

    /**
     * Utility to obtain extension of an url. TODO add test case
     * 
     * @param url
     * @return extension or null if none was found.
     */
    protected static String parseFormatFromUrl(final String url) {
        final int queryDelimiterIndex = url.indexOf('?');
        final String path = (queryDelimiterIndex > 0 ? url.substring(0, queryDelimiterIndex) : url);
        final int formatDelimiterIndex = path.lastIndexOf('.');
        return (formatDelimiterIndex > 0 ? path.substring(formatDelimiterIndex + 1) : null);
    }
}
