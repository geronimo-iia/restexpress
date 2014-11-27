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
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

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
import org.jboss.netty.channel.DefaultFileRegion;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.domain.CharacterSet;
import org.restexpress.pipeline.HttpResponseWriter;
import org.restexpress.util.HttpSpecification;

/**
 * {@link ExperimentalHttpResponseWriter} implements an {@link HttpResponseWriter}.
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
public final class ExperimentalHttpResponseWriter implements HttpResponseWriter {

    /**
     * Build a new instance of {@link ExperimentalHttpResponseWriter}.
     */
    public ExperimentalHttpResponseWriter() {
        super();
    }

    @Override
    public void write(final ChannelHandlerContext ctx, final Request request, final Response response) {
        final HttpResponse httpResponse = new DefaultHttpResponse(request.getHttpVersion(), response.getResponseStatus());
        // add all header
        addHeaders(response, httpResponse);

        RandomAccessFile raf = null;
        long fileLength = 0l;

        try {

            // set content
            if (response.hasBody() && HttpSpecification.isContentAllowed(response)) {
                // If the response body already contains a ChannelBuffer, use it.
                Class<?> bodyClass = response.getBody().getClass();
                if (ChannelBuffer.class.isAssignableFrom(bodyClass)) {
                    httpResponse.setContent(ChannelBuffers.wrappedBuffer((ChannelBuffer) response.getBody()));
                } else if (File.class.isAssignableFrom(bodyClass)) {
                    final File resource = (File) response.getBody();
                    // check for is Modified Since
                    if (isModifiedSince(request, resource)) {
                        response.setResponseStatus(HttpResponseStatus.NOT_MODIFIED);
                        final Calendar time = new GregorianCalendar();
                        response.addHeader(ResponseHeader.DATE.getHeader(), HttpHeaderDateTimeFormat.RFC_1123.format(time.getTime()));
                    } else {
                        // we have little thing to do
                        final Calendar time = new GregorianCalendar();
                        Date currentTime = time.getTime();
                        // date header
                        response.addHeader(ResponseHeader.DATE.getHeader(), HttpHeaderDateTimeFormat.RFC_1123.format(currentTime));
                        // last modified header
                        Date lastModified = new Date(resource.lastModified());
                        if (lastModified.after(currentTime)) {
                            response.addHeader(ResponseHeader.LAST_MODIFIED.getHeader(),
                                    HttpHeaderDateTimeFormat.RFC_1123.format(currentTime));
                        } else {
                            response.addHeader(ResponseHeader.LAST_MODIFIED.getHeader(),
                                    HttpHeaderDateTimeFormat.RFC_1123.format(lastModified));
                        }
                        // content type
                        setContentTypeHeader(httpResponse, resource);
                        // open file

                        raf = new RandomAccessFile(resource, "r");
                        fileLength = raf.length();
                        // we can now add content length header
                        httpResponse.headers().set(ResponseHeader.CONTENT_LENGTH.getHeader(), fileLength);

                        // Write the initial line and the header.
                        ctx.getChannel().write(httpResponse);

                    }

                } else { // response body is assumed to be a string
                    httpResponse
                            .setContent(ChannelBuffers.copiedBuffer(response.getBody().toString(), CharacterSet.UTF_8.getCharset()));
                }
            }

            final ChannelFutureListener channelFutureListener = keepAlive(request, response, httpResponse);

            ChannelFuture contentFuture = null;
            if (raf == null) {
                contentFuture = ctx.getChannel().write(httpResponse);
            } else {
                // Write the content.
                contentFuture = ctx.getChannel().write(new DefaultFileRegion(raf.getChannel(), 0, fileLength));
            }

            // add final listener
            if (channelFutureListener != null)
                contentFuture.addListener(channelFutureListener);
            
        } catch (ParseException e) {
            throw new HttpRuntimeException(HttpResponseStandardStatus.BAD_REQUEST, e);
        } catch (FileNotFoundException e) {
            throw new HttpRuntimeException(HttpResponseStandardStatus.NOT_FOUND, e);
        } catch (IOException e) {
            throw new HttpRuntimeException(HttpResponseStandardStatus.INTERNAL_SERVER_ERROR, e);
        } finally {
            if (raf != null)
                try {
                    raf.close();
                } catch (IOException e) {
                }
        }
    }

    /**
     * @param request
     * @param response
     * @param httpResponse
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
     * Sets the content type header for the HTTP Response
     * 
     * @param response HTTP response
     * @param file file to extract content type
     */
    private static void setContentTypeHeader(HttpResponse httpResponse, File file) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        httpResponse.headers().set(ResponseHeader.CONTENT_TYPE.getHeader(), mimeTypesMap.getContentType(file.getPath()));
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
