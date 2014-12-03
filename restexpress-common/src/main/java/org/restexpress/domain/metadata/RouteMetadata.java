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
/*
 Copyright 2011, Strategic Gains, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.restexpress.domain.metadata;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * {@link RouteMetadata} expose meta data information on route: name, uri, alias, methods, ...
 * 
 * @author toddf
 * @since Jan 31, 2011
 */
@XmlRootElement(name = "route")
public class RouteMetadata implements Serializable {
    private static final long serialVersionUID = -6892754126510965799L;
    /**
     * Route name.
     */
    private String name;
    /**
     * URI information.
     */
    private UriMetadata uri;
    private List<String> aliases;
    private List<String> methods;
    /**
     * Use Serialization if needed.
     */
    private boolean serialized;

    protected RouteMetadata() {
        super();
    }

    public RouteMetadata(String name, UriMetadata uri, List<String> aliases, List<String> methods, boolean isSerialized) {
        super();
        this.name = name;
        this.uri = uri;
        this.aliases = aliases;
        this.methods = methods;
        this.serialized = isSerialized;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public UriMetadata getUri() {
        return uri;
    }

    /**
     * Return URL.
     * 
     * @param baseUrl
     * @return {@link String} url.
     */
    public String getUrl(String baseUrl) {
        return baseUrl + uri.getPattern().replace(".{format}", "");
    }

    @SuppressWarnings("unchecked")
    public List<String> getMethods() {
        return methods == null ? Collections.EMPTY_LIST : methods;
    }

    public boolean isSerialized() {
        return serialized;
    }

    public void setSerialized(final boolean serialized) {
        this.serialized = serialized;
    }

    @SuppressWarnings("unchecked")
    public List<String> getAliases() {
        return aliases == null ? Collections.EMPTY_LIST : Collections.unmodifiableList(aliases);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        RouteMetadata other = (RouteMetadata) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RouteMetadata {name=\"" + name + "\", uri=\"" + uri + " \"}";
    }

}
