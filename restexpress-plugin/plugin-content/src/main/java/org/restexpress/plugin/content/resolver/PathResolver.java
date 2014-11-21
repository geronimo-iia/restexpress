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
package org.restexpress.plugin.content.resolver;

/**
 * {@link PathResolver} resolve name according to location path.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public final class PathResolver {

    /**
     * Associated path
     */
    protected final String path;

    /**
     * Path length (Optimization)
     */
    protected final int pathLength;

    /**
     * Build a new instance of {@link PathResolver}.
     * 
     * @param path path used to resolve name.
     */
    public PathResolver(final String path) {
        this.path = path == null ? "/" : path.equals("") ? "/" : path;
        pathLength = this.path.equals("/") ? 0 : this.path.length();
    }

    /**
     * Extract remote name from specified name by removing configured path.
     * 
     * Assert: name must start with path and not null.
     * 
     * @param name
     * @return remote name or null.
     */
    public String resolve(final String name) {
        return name.length() > pathLength ? pathLength > 0 ? name.substring(pathLength) : name : null;
    }

    /**
     * @return the specified path.
     */
    public String getPath() {
        return path;
    }

    /**
     * @param name
     * @return {@link Boolean.<code>true</code>} if name start with this path.
     */
    public Boolean match(final String name) {
        return name != null ? name.startsWith(path) : Boolean.FALSE;
    }

    @Override
    public String toString() {
        return "PathResolver [path=" + path + ", pathLength=" + pathLength + "]";
    }

}
