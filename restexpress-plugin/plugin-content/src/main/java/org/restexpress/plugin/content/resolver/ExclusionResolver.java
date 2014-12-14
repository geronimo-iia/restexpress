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
package org.restexpress.plugin.content.resolver;

import java.util.Arrays;

/**
 * {@link ExclusionResolver} implements exclusion path.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public final class ExclusionResolver {
    /**
     * Array of excluded path.
     */
    private final String[] excludedPath;
    /**
     * Number of occurrence in excludedPath (Optimization).
     */
    protected final int excludedPathLength;

    /**
     * Build a new instance of {@link ExclusionResolver}.
     * 
     * @param excludedPath array of excluded path.
     */
    public ExclusionResolver(final String[] excludedPath) {
        this.excludedPath = excludedPath;
        excludedPathLength = excludedPath != null ? excludedPath.length : 0;
    }

    /**
     * @param name
     * @return {@link Boolean.TRUE} if name is excluded.
     */
    public boolean isExcluded(final String name) {
        boolean result = false;
        if (name != null)
            for (int i = 0; i < excludedPathLength && !result; i++)
                result = name.startsWith(excludedPath[i]);
        return result;
    }

    @Override
    public String toString() {
        return "ExclusionResolver [excludedPath=" + Arrays.toString(excludedPath) + ", excludedPathLength=" + excludedPathLength + "]";
    }

}
