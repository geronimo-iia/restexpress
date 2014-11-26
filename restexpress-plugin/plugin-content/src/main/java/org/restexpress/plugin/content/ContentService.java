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

/**
 * {@link ContentEntryPoint} declare service content operation.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public interface ContentService {

    
    /**
     * @return main entry point
     */
    public String entryPoint();
    
    /**
     * Enable caching with default parameters:
     * <ul>
     * <li>initial capacity: 3000</li>
     * <li>maximum size: 25000</li>
     * <li>expire after write: 10 minutes</li>
     * </ul>
     */
    public void enableCache();

    /**
     * Enable cache.
     * 
     * @param initialCapacity initial Capacity
     * @param maximumSiz emaximum Size
     * @param expireAfterWrite time expire After Write in Minutes
     */
    public void enableCache(final int initialCapacity, final int maximumSize, final int expireAfterWrite);

    /**
     * Disable cache.
     */
    public void disableCache();
    
    /**
     * @return current temporary directory
     */
    public File temporaryDirectory();

    /**
     * Register a specific {@link ContextAdapter} instance.
     * 
     * @param contextAdapter
     */
    public void register(ContextAdapter contextAdapter);

    /**
     * @param name name of {@link ContextAdapter}
     * @return associated {@link ContextAdapter} instance or null if none was named with specified name.
     */
    public ContextAdapter find(String name);

    /**
     * @param name name of {@link ContextAdapter}
     * @return true if a {@link ContextAdapter} was removed.
     */
    public boolean remove(String name);
}
