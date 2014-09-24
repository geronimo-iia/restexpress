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
package org.restexpress;

import java.util.Set;

import org.restexpress.response.ResponseProcessorManager;
import org.restexpress.response.ResponseWrapper;
import org.restexpress.serialization.Processor;

/**
 * {@link SerializationProvider} define methods to manage serialization configuration.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public interface SerializationProvider {

    /**
     * Add the specified {@link Processor} and {@link ResponseWrapper}.
     * 
     * @param org .restexpress.processor
     * @param responseWrapper
     * @return this {@link ResponseProcessorManager} instance.
     */
    public abstract SerializationProvider add(Processor processor, ResponseWrapper responseWrapper);

    /**
     * Add the specified {@link Processor} and {@link ResponseWrapper}.
     * 
     * @param org .restexpress.processor
     * @param responseWrapper
     * @param isDefault
     * @return this {@link ResponseProcessorManager} instance.
     */
    public abstract SerializationProvider add(Processor processor, ResponseWrapper responseWrapper, boolean isDefault);

    /**
     * @param mimeType
     * @return {@link Processor} instance for specified mime type or null if not exists.
     */
    public abstract Processor processor(String mimeType);

    /**
     * @return default {@link Processor}.
     */
    public abstract Processor defaultProcessor();

    /**
     * @return a {@link Set} of supported media type.
     */
    public Set<String> supportedMediaType();
}