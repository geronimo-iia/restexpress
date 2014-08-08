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
package org.restexpress.domain.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.intelligentsia.commons.http.exception.Exceptions;
import org.intelligentsia.commons.http.exception.HttpRuntimeException;

/**
 * ErrorResult is a wrapper class to deal with restexpress error return for java client.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
@XmlRootElement(name="response")
public class ErrorResult implements Serializable {

    private static final long serialVersionUID = -3700889923122706476L;
    /**
     * HTTP response status code. It's a facility to keep status code here.
     */
    private int httpStatus;
    /**
     * Associated error message.
     */
    private String message;
    /**
     * class name of root cause.
     */
    private String errorType;

    /**
     * Build a new instance of {@link ErrorResult}.
     */
    public ErrorResult() {
        super();
    }

    public ErrorResult(final int httpStatusCode, final String message, final String errorType) {
        super();
        this.httpStatus = httpStatusCode;
        this.message = message;
        this.errorType = errorType;
    }

    /**
     * @return an {@link HttpRuntimeException} associated with this error.
     */
    public HttpRuntimeException toHttpRuntimeException() {
        return Exceptions.getExceptionFor(httpStatus, message, errorType != null ? new RuntimeException(errorType) : null);
    }

    @Override
    public String toString() {
        return "ErrorResult {httpStatusCode=\"" + httpStatus + "\", message=\"" + message + "\", errorType=\"" + errorType + "\"}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + httpStatus;
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
        ErrorResult other = (ErrorResult) obj;
        if (httpStatus != other.httpStatus)
            return false;
        return true;
    }

}
