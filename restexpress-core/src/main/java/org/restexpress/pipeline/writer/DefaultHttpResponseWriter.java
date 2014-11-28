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
package org.restexpress.pipeline.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.intelligentsia.commons.http.HttpHeaderDateTimeFormat;
import org.intelligentsia.commons.http.RequestHeader;
import org.intelligentsia.commons.http.ResponseHeader;
import org.intelligentsia.commons.http.exception.HttpRuntimeException;
import org.intelligentsia.commons.http.status.HttpResponseStandardStatus;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.Format;
import org.restexpress.pipeline.HttpResponseWriter;
import org.restexpress.response.ResponseProcessorManager;
import org.restexpress.util.HttpSpecification;

/**
 * {@link DefaultHttpResponseWriter} implements an {@link HttpResponseWriter}.
 * 
 * 
 * <ul>
 * <li>If response.getBody() return a {@link ChannelBuffer} then we used it</li>
 * <li>if response.getBody() return a {@link File} the we use a special code to transfer it</li>
 * <li>else we assume that object is a string</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Aug 26, 2010
 */
public final class DefaultHttpResponseWriter implements HttpResponseWriter {

    private final Map<String, String> format;

    /**
     * Build a new instance of {@link DefaultHttpResponseWriter}.
     */
    public DefaultHttpResponseWriter() {
        super();
        format = Format.toMap();
    }

    @Override
    public void write(final ChannelHandlerContext ctx, final Request request, final Response response) {
        final HttpResponse httpResponse = new DefaultHttpResponse(request.getHttpVersion(), response.getResponseStatus());

        // add all header
        addHeaders(response, httpResponse);

        // manage content
        File resource = null;
        try {
            // set content
            if (response.hasBody() && HttpSpecification.isContentAllowed(response)) {
                // If the response body already contains a ChannelBuffer, use it.
                Class<?> bodyClass = response.getBody().getClass();
                if (ChannelBuffer.class.isAssignableFrom(bodyClass)) {
                    httpResponse.setContent(ChannelBuffers.wrappedBuffer((ChannelBuffer) response.getBody()));
                } else if (File.class.isAssignableFrom(bodyClass)) {
                    resource = (File) response.getBody();
                    processFileResponseHeader(request, response, httpResponse, resource);
                } else { // response body is assumed to be a string
                    httpResponse
                            .setContent(ChannelBuffers.copiedBuffer(response.getBody().toString(), CharacterSet.UTF_8.getCharset()));
                }
            }

            // find witch ChannelFutureListener to use
            final ChannelFutureListener channelFutureListener = keepAlive(request, response, httpResponse);

            // write the content
            writeContent(ctx, httpResponse, resource, channelFutureListener);

        } catch (ParseException e) {
            throw new HttpRuntimeException(HttpResponseStandardStatus.BAD_REQUEST, e);
        } catch (FileNotFoundException e) {
            throw new HttpRuntimeException(HttpResponseStandardStatus.NOT_FOUND, e);
        }
    }

    private void processFileResponseHeader(final Request request, final Response response, final HttpResponse httpResponse,
            File resource) throws ParseException {
        // check for is Modified Since
        if (isModifiedSince(request, resource)) {
            response.setResponseStatus(HttpResponseStatus.NOT_MODIFIED);
            final Calendar time = new GregorianCalendar();
            httpResponse.headers().set(ResponseHeader.DATE.getHeader(), HttpHeaderDateTimeFormat.RFC_1123.format(time.getTime()));
        } else {
            // we have little thing to do
            final Calendar time = new GregorianCalendar();
            Date currentTime = time.getTime();
            // date header
            httpResponse.headers().set(ResponseHeader.DATE.getHeader(), HttpHeaderDateTimeFormat.RFC_1123.format(currentTime));
            // last modified header
            Date lastModified = new Date(resource.lastModified());
            if (lastModified.after(currentTime)) {
                httpResponse.headers().set(ResponseHeader.LAST_MODIFIED.getHeader(),
                        HttpHeaderDateTimeFormat.RFC_1123.format(currentTime));
            } else {
                httpResponse.headers().set(ResponseHeader.LAST_MODIFIED.getHeader(),
                        HttpHeaderDateTimeFormat.RFC_1123.format(lastModified));
            }
            // content type
            String extension = ResponseProcessorManager.parseFormatFromUrl(resource.getName());
            String mediaType = Format.BIN.getMediaType();
            if (extension != null) {
                mediaType = format.get(extension);
            }
            httpResponse.headers().set(ResponseHeader.CONTENT_TYPE.getHeader(), mediaType);

            // we can now add content length header
            httpResponse.headers().set(ResponseHeader.CONTENT_LENGTH.getHeader(), resource.length());
        }
    }

    private static void writeContent(final ChannelHandlerContext ctx, final HttpResponse httpResponse, final File resource,
            final ChannelFutureListener channelFutureListener) throws FileNotFoundException {
        final ChannelFuture contentFuture;
        if (resource == null) {
            contentFuture = ctx.getChannel().write(httpResponse);
            // add final listener
            if (channelFutureListener != null)
                contentFuture.addListener(channelFutureListener);
        } else {
            ChannelFuture writeFuture = ctx.getChannel().write(httpResponse);
            writeFuture.addListener(new FileWritingChannelFutureListener(resource, channelFutureListener));
        }
    }

    /**
     * @param request {@link Request}
     * @param response {@link Response}
     * @param httpResponse {@link HttpResponse}
     * @return instance of {@link ChannelFutureListener} to use.
     */
    private static ChannelFutureListener keepAlive(final Request request, final Response response, final HttpResponse httpResponse) {
        ChannelFutureListener channelFutureListener;
        if (request.isKeepAlive()) {
            // Add 'Content-Length' header only for a keep-alive connection.
            if (HttpSpecification.isContentLengthAllowed(response)
                    && !httpResponse.headers().contains(ResponseHeader.CONTENT_LENGTH.getHeader())) {
                httpResponse.headers().set(ResponseHeader.CONTENT_LENGTH.getHeader(),
                        String.valueOf(httpResponse.getContent().readableBytes()));
            }
            // Support "Connection: Keep-Alive" for HTTP 1.0 requests.
            if (request.isHttpVersion1_0()) {
                httpResponse.headers().add(ResponseHeader.CONNECTION.getHeader(), "Keep-Alive");
            }
            channelFutureListener = ChannelFutureListener.CLOSE_ON_FAILURE;
        } else {
            httpResponse.headers().set(ResponseHeader.CONNECTION.getHeader(), "close");
            // Close the connection as soon as the message is sent.
            channelFutureListener = ChannelFutureListener.CLOSE;
        }
        return channelFutureListener;
    }

    /**
     * Adds all {@link Response} header into {@link HttpResponse}.
     * 
     * @param response {@link Response}
     * @param httpResponse {@link HttpResponse}
     */
    private static void addHeaders(final Response response, final HttpResponse httpResponse) {
        for (Map.Entry<String, List<String>> entry : response.headers().entrySet()) {
            httpResponse.headers().add(entry.getKey(), entry.getValue());
        }
    }

    /**
     * @param request
     * @param resource
     * @return True if resource is modified since date value read from {@link RequestHeader#IF_MODIFIED_SINCE}.
     * @throws ParseException
     */
    private static boolean isModifiedSince(final Request request, final File resource) throws ParseException {
        final String ifModifiedSince = request.getHeader(RequestHeader.IF_MODIFIED_SINCE.getHeader());
        if (ifModifiedSince != null && !ifModifiedSince.isEmpty()) {
            final Date ifModifiedSinceDate = HttpHeaderDateTimeFormat.parseAny(ifModifiedSince);
            // just compare second
            final long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
            final long fileLastModifiedSeconds = resource.lastModified() / 1000;
            return ifModifiedSinceDateSeconds <= fileLastModifiedSeconds;
        }
        return false;
    }
}
