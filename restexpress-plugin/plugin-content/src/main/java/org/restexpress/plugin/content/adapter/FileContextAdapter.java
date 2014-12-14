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
package org.restexpress.plugin.content.adapter;

import java.io.File;
import java.io.IOException;

import org.restexpress.plugin.content.ContextAdapter;
import org.restexpress.plugin.content.resolver.Resolver;

import com.google.common.base.Preconditions;

/**
 * {@link FileContextAdapter} implement a {@link ContextAdapter} which map an external folder.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class FileContextAdapter implements ContextAdapter {

    /**
     * {@link Resolver}.
     */
    protected final Resolver resolver;

    /**
     * {@link File} as root directory
     */
    protected final File rootDirectory;

    /**
     * Convenient name.
     */
    protected final String name;

    public FileContextAdapter(final String name, final Resolver resolver, final File rootDirectory) {
        super();
        this.name = name;
        this.resolver = Preconditions.checkNotNull(resolver);
        this.rootDirectory = rootDirectory;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Boolean match(final String name) {
        return resolver.match(name);
    }

    @Override
    public File retrieve(final String name) throws IOException {
        // extract path name
        final String pathname = resolver.resolve(name);
        // locate file under root directory
        final File file = new File(rootDirectory, pathname);
        // check file if exists and visible
        if (file.isHidden() || !file.exists())
            return null;
        if (!file.isFile())
            return null;
        return file;
    }

    @Override
    public String toString() {
        return "FileContextAdapter [resolver=" + resolver + ", rootDirectory=" + rootDirectory + "]";
    }

}
