/**
 * ====
 *            Licensed to the Apache Software Foundation (ASF) under one
 *            or more contributor license agreements.  See the NOTICE file
 *            distributed with this work for additional information
 *            regarding copyright ownership.  The ASF licenses this file
 *            to you under the Apache License, Version 2.0 (the
 *            "License"); you may not use this file except in compliance
 *            with the License.  You may obtain a copy of the License at
 *
 *              http://www.apache.org/licenses/LICENSE-2.0
 *
 *            Unless required by applicable law or agreed to in writing,
 *            software distributed under the License is distributed on an
 *            "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *            KIND, either express or implied.  See the License for the
 *            specific language governing permissions and limitations
 *            under the License.
 *
 * ====
 *
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
package org.intelligentsia.commons.http;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * HttpHeaderDateTimeFormat.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public enum HttpHeaderDateTimeFormat {
    // We should use other tool like "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    // joda or ISO8601Utils from jackson-databind
    ISO_8601("not used") {

        @Override
        public Date parse(String source) throws ParseException {
            return ISO8601Utils.parse(source, new ParsePosition(0));
        }

        @Override
        public String format(Date date) {
            return ISO8601Utils.format(date, true);
        }

    }, //
    RFC_1123("EEE, dd MMM yyyy HH:mm:ss 'GMT'"), //
    RFC_822("EEE, dd MMM yyyy HH:mm:ss z"), //
    RFC_850("EEEE, dd-MMM-yy HH:mm:ss z"), //
    AINSI_C_ASCTIME("EEE MMM d HH:mm:ss yyyy"), //
    DEFAULT_DATE("yyyy-MM-dd");

    private static TimeZone UNIVERSAL_TIMEZONE = TimeZone.getTimeZone("UTC");

    private String format;

    private HttpHeaderDateTimeFormat(final String format) {
        this.format = format;
    }

    /**
     * @return {@link DateFormat} instance for the selected format
     */
    protected DateFormat getDateFormat() {
        final DateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        dateFormat.setTimeZone(UNIVERSAL_TIMEZONE);
        return dateFormat;
    }

    /**
     * @param source
     * @return A Date parsed from the string.
     * @throws ParseException if the beginning of the specified string cannot be parsed.
     */
    public Date parse(final String source) throws ParseException {
        return getDateFormat().parse(source);
    }

    /**
     * @param date
     * @return the formatted time string.
     */
    public String format(final Date date) {
        return getDateFormat().format(date);
    }

    /**
     * @param source
     * @return Date parsed from the string.
     * @throws ParseException if no format can be applied to obtain a Date.
     */
    public static Date parseAny(final String source) throws ParseException {
        ParseException lastException = null;
        for (final HttpHeaderDateTimeFormat formater : HttpHeaderDateTimeFormat.values()) {
            try {
                return formater.parse(source);
            } catch (final ParseException e) {
                // Keep the first exception
                lastException = lastException == null ? e : lastException;
            }
        }
        // none
        throw lastException;
    }

    /**
     * @param date
     * @return a formatted time string according RFC 1123
     */
    public static String defaultFormat(final Date date) {
        return HttpHeaderDateTimeFormat.RFC_1123.format(date);
    }
}
