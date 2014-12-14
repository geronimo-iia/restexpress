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

/**
 * {@link Resolver} manage exclusion and resolve path management.
 * 
 */
public class Resolver {

    /**
     * {@link PathResolver}.
     */
    protected final PathResolver pathResolver;

    /**
     * {@link ExclusionResolver}.
     */
    protected final ExclusionResolver exclusionResolver;

    /**
     * Build a new instance of {@link WebRemoteClient}.
     * 
     * @param path location associated with this remote ('/' or empty string stand for root).
     * @param excludedPath array of excluded path.
     * @throws NullPointerException if path is null
     */
    public Resolver(final String path, final String[] excludedPath) {
        super();
        pathResolver = new PathResolver(path);
        exclusionResolver = new ExclusionResolver(excludedPath);
    }

    /**
     * @param name
     * @return {@link Boolean.TRUE} if named resource is not excluded and match path.
     */
    public Boolean match(final String name) {
        return !exclusionResolver.isExcluded(name) && pathResolver.match(name);
    }

    /**
     * Extract remote name from specified name by removing configured path.
     * 
     * Assert: name must start with path and not null ({@link Resolver#match(String)} == {@link Boolean#TRUE}).
     * 
     * @param name
     * @return remote name or null.
     */
    public String resolve(final String name) {
        return pathResolver.resolve(name);
    }

    @Override
    public String toString() {
        return "Resolver [pathResolver=" + pathResolver + ", exclusionResolver=" + exclusionResolver + "]";
    }

}
