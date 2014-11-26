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
package org.restexpress.plugin.content;

import java.io.File;
import java.io.IOException;

/**
 * {@link ContextAdapter} declare method to adapt resource origin by context.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public interface ContextAdapter {

    /**
     * @return convenient name
     */
    public String name();
    
    
    /**
     * @param name
     * @return {@link Boolean.TRUE} if named resource can be retrieved by this {@link ContextAdapter}.
     */
    public Boolean match(final String name);

    /**
     * Retrieve a file.
     * 
     * @param name
     * @return {@link File} instance or null if not found or an error occurs
     * @throws IOException
     */
    public File retrieve(final String name) throws IOException;

}
