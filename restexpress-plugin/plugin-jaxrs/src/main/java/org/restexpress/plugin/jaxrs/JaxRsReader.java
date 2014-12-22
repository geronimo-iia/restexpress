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
package org.restexpress.plugin.jaxrs;

import java.lang.reflect.Method;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.restexpress.RestExpress;

/**
 * {@link JaxRsReader} read class.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class JaxRsReader {

    /**
     * {@link RestExpress} configuration service.
     */
    RestExpress restExpress;

    public void read(Class<?> cls) {
        Path path = cls.getAnnotation(Path.class);
        
        for (Method method : cls.getMethods()) {
            Path methodPath = method.getAnnotation(Path.class);
            HEAD head = method.getAnnotation(HEAD.class);
            GET get = method.getAnnotation(GET.class);
            POST post = method.getAnnotation(POST.class);
            PUT put = method.getAnnotation(PUT.class);
            DELETE delete = method.getAnnotation(DELETE.class);
            
            
        }
        
    }

}
