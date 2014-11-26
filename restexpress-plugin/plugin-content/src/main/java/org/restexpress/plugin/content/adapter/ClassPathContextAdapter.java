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
import java.io.InputStream;

import org.restexpress.plugin.content.ContextAdapter;
import org.restexpress.plugin.content.resolver.Resolver;

/**
 * {@link ClassPathContextAdapter} implement a {@link ContextAdapter} which retrieve resource from class path.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class ClassPathContextAdapter extends AbstractContextAdapter<ClassLoader> {

    public ClassPathContextAdapter(String name, Resolver resolver, String remoteDocumentBase, File tempDirectory) {
        super(name, resolver, remoteDocumentBase, tempDirectory);
    }

    @Override
    protected ClassLoader createContext() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    protected void destroyContext(ClassLoader context) {
        // nothing to do
    }

    @Override
    protected boolean exists(ClassLoader context, String remotePath) throws IOException {
        return context.getResource(remotePath) != null;
    }

    @Override
    protected InputStream get(ClassLoader context, String remotePath) throws IOException {
        return context.getResourceAsStream(remotePath);
    }

    public class ClassPathContext {
        ClassLoader classLoader;
    }
}
