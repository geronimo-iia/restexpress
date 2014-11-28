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
package org.restexpress.serialization;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;

/**
 * {@link AbstractProcessor} implements basic functionality of {@link Processor}
 * :
 * <ul>
 * <li>manage supported media type</li>
 * <li>define utility methods to obtain {@link OutputStreamWriter} and
 * {@link InputStreamReader} from {@link ChannelBuffer}</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public abstract class AbstractProcessor implements Processor {

	private final List<String> supportedMediaType;

	/**
	 * {@link Charset} used to get {@link OutputStreamWriter} and
	 * {@link InputStreamReader}. By default it's {@link CharacterSet#UTF_8}.
	 */
	protected Charset charset = CharacterSet.UTF_8.getCharset();

	/**
	 * Build a new instance of {@link AbstractProcessor}.
	 * 
	 * @param mediaTypes
	 *            list of {@link MediaType}.
	 * @throws IllegalArgumentException
	 *             if mediaTypes is null or empty
	 */
	public AbstractProcessor(MediaType... mediaTypes) throws IllegalArgumentException {
		super();
		if (mediaTypes == null)
			throw new IllegalArgumentException("mediaTypes can not be null");
		if (mediaTypes.length == 0)
			throw new IllegalArgumentException("mediaTypes can not empty");
		this.supportedMediaType = new ArrayList<String>();
		for (MediaType mediaType : mediaTypes) {
			supportedMediaType.add(mediaType.getMime());
		}
	}

	/**
	 * Build a new instance of {@link AbstractProcessor}.
	 * 
	 * @param mediaTypes
	 *            list of media type.
	 * @throws IllegalArgumentException
	 *             if mediaTypes is null or empty
	 */
	public AbstractProcessor(String... mediaTypes) throws IllegalArgumentException {
		super();
		if (mediaTypes == null)
			throw new IllegalArgumentException("mediaTypes can not be null");
		if (mediaTypes.length == 0)
			throw new IllegalArgumentException("mediaTypes can not empty");
		this.supportedMediaType = new ArrayList<String>();
		for (String mediaType : mediaTypes) {
			supportedMediaType.add(mediaType);
		}
	}

	/**
	 * Build a new instance of {@link AbstractProcessor}.
	 * 
	 * @param mediaTypes
	 *            list of supported media type.
	 * @throws IllegalArgumentException
	 *             if mediaTypes is null or empty
	 */
	public AbstractProcessor(List<String> mediaTypes) throws IllegalArgumentException {
		super();
		if (mediaTypes == null)
			throw new IllegalArgumentException("mediaTypes can not be null");
		if (mediaTypes.isEmpty())
			throw new IllegalArgumentException("mediaTypes can not empty");
		this.supportedMediaType = new ArrayList<String>();
		this.supportedMediaType.addAll(mediaTypes);
	}

	/**
	 * Adds specified mime if not ever added before. This method enable easy
	 * customization of {@link Processor} and should only be called in
	 * constructor.
	 * 
	 * @param mime
	 * @throws IllegalArgumentException
	 *             if mime is null or empty
	 */
	protected void addMediaType(String mime) throws IllegalArgumentException {
		if (mime == null)
			throw new IllegalArgumentException("mime can't be null");
		if (mime.isEmpty())
			throw new IllegalArgumentException("mime can't be empty");
		if (!supportedMediaType.contains(mime))
			supportedMediaType.add(mime);
	}

	@Override
	public List<String> supportedMediaTypes() {
		return supportedMediaType;
	}

	@Override
	public String mediaType() {
		return supportedMediaType.get(0);
	}

	/**
	 * @param buffer
	 * @return an {@link OutputStreamWriter} with specified {@link Charset}.
	 * @see AbstractProcessor#charset
	 */
	protected OutputStreamWriter getOutputStreamWriter(ChannelBuffer buffer) {
		return new OutputStreamWriter(new ChannelBufferOutputStream(buffer), charset);
	}

	/**
	 * @param buffer
	 * @return an {@link InputStreamReader} with specified {@link Charset}.
	 * @see AbstractProcessor#charset
	 */
	protected InputStreamReader getInputStreamReader(ChannelBuffer buffer) {
		return new InputStreamReader(new ChannelBufferInputStream(buffer), charset);
	}

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [supportedMediaType=" + supportedMediaType + ", charset=" + charset + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((charset == null) ? 0 : charset.hashCode());
        result = prime * result + ((supportedMediaType == null) ? 0 : supportedMediaType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractProcessor other = (AbstractProcessor) obj;
        if (charset == null) {
            if (other.charset != null)
                return false;
        } else if (!charset.equals(other.charset))
            return false;
        if (supportedMediaType == null) {
            if (other.supportedMediaType != null)
                return false;
        } else if (!supportedMediaType.equals(other.supportedMediaType))
            return false;
        return true;
    }
	
	
}
