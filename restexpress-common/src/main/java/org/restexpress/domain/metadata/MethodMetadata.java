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
package org.restexpress.domain.metadata;

import java.io.Serializable;

/**
 * {@link MethodMetadata} expose all meta data on specific method.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class MethodMetadata implements Serializable {

    private static final long serialVersionUID = 7767397795601153835L;
    private String alias;
    private String method;
    private boolean serialized;

    protected MethodMetadata() {
        super();
    }

    public MethodMetadata(String alias, String method, boolean serialized) {
        super();
        this.alias = alias;
        this.method = method;
        this.serialized = serialized;
    }

    public final String getAlias() {
        return alias;
    }

    public final void setAlias(String alias) {
        this.alias = alias;
    }

    public final String getMethod() {
        return method;
    }

    public final void setMethod(String method) {
        this.method = method;
    }

    public final boolean isSerialized() {
        return serialized;
    }

    public final void setSerialized(boolean serialized) {
        this.serialized = serialized;
    }

    @Override
    public String toString() {
        return "MethodMetadata [alias=" + alias + ", method=" + method + ", serialized=" + serialized + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alias == null) ? 0 : alias.hashCode());
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + (serialized ? 1231 : 1237);
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
        MethodMetadata other = (MethodMetadata) obj;
        if (alias == null) {
            if (other.alias != null)
                return false;
        } else if (!alias.equals(other.alias))
            return false;
        if (method == null) {
            if (other.method != null)
                return false;
        } else if (!method.equals(other.method))
            return false;
        if (serialized != other.serialized)
            return false;
        return true;
    }

}
