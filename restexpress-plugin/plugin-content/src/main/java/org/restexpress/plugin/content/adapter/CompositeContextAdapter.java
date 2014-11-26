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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.restexpress.plugin.content.ContextAdapter;

import com.google.common.base.Preconditions;

/**
 * {@link CompositeContextAdapter} implement a composite {@link ContextAdapter}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class CompositeContextAdapter implements ContextAdapter, Iterable<ContextAdapter> {

    protected ContextAdapter[] clients;

    private final int size;

    /**
     * Build a new instance of {@link CompositeContextAdapter}.
     * 
     * @param clients {@link Collection} of {@link ContextAdapter} client to add
     * @throws NullPointerException if {@code clients} is null
     * @throws IllegalArgumentException if {@code clients} is empty
     */
    public CompositeContextAdapter(final Collection<ContextAdapter> clients) {
        super();
        Preconditions.checkArgument(Preconditions.checkNotNull(clients).size() > 0);
        this.clients = clients.toArray(new AbstractContextAdapter<?>[clients.size()]);
        size = this.clients.length;
    }

    /**
     * Build a new instance of {@link CompositeContextAdapter}.
     * 
     * @param clients array of {@link ContextAdapter} client to add
     * @throws NullPointerException if {@code clients} is null
     * @throws IllegalArgumentException if {@code clients} is empty
     */
    public CompositeContextAdapter(final ContextAdapter[] clients) {
        super();
        Preconditions.checkArgument(Preconditions.checkNotNull(clients).length > 0);
        this.clients = clients;
        size = this.clients.length;
    }

    @Override
    public String name() {
        return getClass().getSimpleName();
    }

    @Override
    public Boolean match(final String name) {
        int i = 0;
        boolean result = false;
        do {
            result = clients[i].match(name);
            i++;
        } while (!result && i < size);
        return result;
    }

    @Override
    public File retrieve(final String name) throws IOException {
        int i = 0;
        File result = null;
        do {
            if (clients[i].match(name))
                result = clients[i].retrieve(name);
            i++;
        } while (result == null && i < size);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [clients=" + Arrays.toString(clients) + "]";
    }

    @Override
    public Iterator<ContextAdapter> iterator() {
        return Arrays.asList(clients).iterator();
    }

    /**
     * @return an array of {@link ContextAdapter}
     */
    public ContextAdapter[] clients() {
        return clients;
    }
}
