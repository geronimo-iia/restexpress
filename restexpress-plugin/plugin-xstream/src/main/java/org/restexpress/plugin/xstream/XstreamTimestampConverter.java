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
 * Copyright 2010, Strategic Gains, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.restexpress.plugin.xstream;

import java.text.ParseException;
import java.util.Date;

import org.intelligentsia.commons.http.HttpHeaderDateTimeFormat;

import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 * @author toddf
 * @since Dec 16, 2010
 */
public class XstreamTimestampConverter implements SingleValueConverter {

    public XstreamTimestampConverter() {
        super();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean canConvert(final Class aClass) {
        return Date.class.isAssignableFrom(aClass);
    }

    @Override
    public Object fromString(final String value) {
        try {
            return HttpHeaderDateTimeFormat.parseAny(value);
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String toString(final Object date) {
        return HttpHeaderDateTimeFormat.ISO_8601.format((Date) date);
    }
}
