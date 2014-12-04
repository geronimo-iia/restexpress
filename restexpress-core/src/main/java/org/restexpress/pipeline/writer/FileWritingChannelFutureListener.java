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
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.DefaultHttpChunk;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Closeables;

/**
 * {@link FileWritingChannelFutureListener} send a file with {@link HttpChunk}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public final class FileWritingChannelFutureListener implements ChannelFutureListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileWritingChannelFutureListener.class);

    private final ChannelFutureListener channelFutureListener;

    private final FileInputStream inputStream;

    private final long contentLength;

    private final ChannelBuffer buffer;

    private long offset = 0;

    /**
     * @param resource {@link File} to send
     * @param channelFutureListener {@link ChannelFutureListener} to use at the end of transfer
     * @throws FileNotFoundException should not occurs
     */
    public FileWritingChannelFutureListener(File resource, final ChannelFutureListener channelFutureListener)
            throws FileNotFoundException {
        this.inputStream = new FileInputStream(resource);
        this.contentLength = resource.length();
        this.channelFutureListener = channelFutureListener;
        if (contentLength < 64 * 1024) {
            buffer = ChannelBuffers.buffer(4096);
        } else {
            buffer = ChannelBuffers.buffer(4096 * 4);
        }
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (!future.isSuccess()) {
            LOGGER.warn("File transfer did not complete: () ", future.getCause().toString());
            future.getChannel().close();
            Closeables.closeQuietly(inputStream);
            return;
        }

        buffer.clear();
        buffer.writeBytes(inputStream, (int) Math.min(contentLength - offset, buffer.writableBytes()));

        offset += buffer.writerIndex();

        HttpChunk chunk = new DefaultHttpChunk(buffer);
        ChannelFuture chunkWriteFuture = future.getChannel().write(chunk);
        // LOGGER.debug("Written {} of {}", offset, contentLength);

        if (offset < contentLength) {
            chunkWriteFuture.addListener(this);
        } else {
            ChannelFuture finalFuture = chunkWriteFuture.getChannel().write(HttpChunk.LAST_CHUNK);
            finalFuture.addListener(channelFutureListener);
            Closeables.closeQuietly(inputStream);
        }
    }
}