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
package org.restexpress.http;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.restexpress.http.ISO8601Utils;

import junit.framework.TestCase;

/**
 * from com.fasterxml.jackson.databind
 * 
 * @see ISO8601Utils
 */
public class ISO8601UtilsTest extends TestCase {
    private Date date;
    private Date dateWithoutTime;
    private Date dateZeroMillis;
    private Date dateZeroSecondAndMillis;

    @Override
    public void setUp() {
        Calendar cal = new GregorianCalendar(2007, 8 - 1, 13, 19, 51, 23);
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.MILLISECOND, 789);
        date = cal.getTime();
        cal.set(Calendar.MILLISECOND, 0);
        dateZeroMillis = cal.getTime();
        cal.set(Calendar.SECOND, 0);
        dateZeroSecondAndMillis = cal.getTime();

        cal = new GregorianCalendar(2007, 8 - 1, 13, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        dateWithoutTime = cal.getTime();

    }

    public void testFormat() {
        String result = ISO8601Utils.format(date);
        assertEquals("2007-08-13T19:51:23Z", result);
    }

    public void testFormatMillis() {
        String result = ISO8601Utils.format(date, true);
        assertEquals("2007-08-13T19:51:23.789Z", result);

        result = ISO8601Utils.format(date, false);
        assertEquals("2007-08-13T19:51:23Z", result);
    }

    public void testFormatTimeZone() {
        String result = ISO8601Utils.format(date, false, TimeZone.getTimeZone("GMT+02:00"));
        assertEquals("2007-08-13T21:51:23+02:00", result);
        result = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT+02:00"));
        assertEquals("2007-08-13T21:51:23.789+02:00", result);
        result = ISO8601Utils.format(date, true, TimeZone.getTimeZone("GMT"));
        assertEquals("2007-08-13T19:51:23.789Z", result);
    }

    public void testParse() throws java.text.ParseException {
        Date d = ISO8601Utils.parse("2007-08-13T19:51:23.789Z", new ParsePosition(0));
        assertEquals(date, d);

        d = ISO8601Utils.parse("2007-08-13T19:51:23Z", new ParsePosition(0));
        assertEquals(dateZeroMillis, d);

        d = ISO8601Utils.parse("2007-08-13T21:51:23.789+02:00", new ParsePosition(0));
        assertEquals(date, d);
    }

    public void testParseShortDate() throws java.text.ParseException {
        Date d = ISO8601Utils.parse("20070813T19:51:23.789Z", new ParsePosition(0));
        assertEquals(date, d);

        d = ISO8601Utils.parse("20070813T19:51:23Z", new ParsePosition(0));
        assertEquals(dateZeroMillis, d);

        d = ISO8601Utils.parse("20070813T21:51:23.789+02:00", new ParsePosition(0));
        assertEquals(date, d);
    }

    public void testParseShortTime() throws java.text.ParseException {
        Date d = ISO8601Utils.parse("2007-08-13T195123.789Z", new ParsePosition(0));
        assertEquals(date, d);

        d = ISO8601Utils.parse("2007-08-13T195123Z", new ParsePosition(0));
        assertEquals(dateZeroMillis, d);

        d = ISO8601Utils.parse("2007-08-13T215123.789+02:00", new ParsePosition(0));
        assertEquals(date, d);
    }

    public void testParseShortDateTime() throws java.text.ParseException {
        Date d = ISO8601Utils.parse("20070813T195123.789Z", new ParsePosition(0));
        assertEquals(date, d);

        d = ISO8601Utils.parse("20070813T195123Z", new ParsePosition(0));
        assertEquals(dateZeroMillis, d);

        d = ISO8601Utils.parse("20070813T215123.789+02:00", new ParsePosition(0));
        assertEquals(date, d);
    }

    public void testParseWithoutTime() throws ParseException {
        Date d = ISO8601Utils.parse("2007-08-13Z", new ParsePosition(0));
        assertEquals(dateWithoutTime, d);

        d = ISO8601Utils.parse("20070813Z", new ParsePosition(0));
        assertEquals(dateWithoutTime, d);

        d = ISO8601Utils.parse("2007-08-13+00:00", new ParsePosition(0));
        assertEquals(dateWithoutTime, d);

        d = ISO8601Utils.parse("20070813+00:00", new ParsePosition(0));
        assertEquals(dateWithoutTime, d);
    }

    public void testParseWithoutTimeAndTimeZoneMustFail() {
        try {
            ISO8601Utils.parse("2007-08-13", new ParsePosition(0));
            fail();
        } catch (ParseException p) {
        }
        try {
            ISO8601Utils.parse("20070813", new ParsePosition(0));
            fail();
        } catch (ParseException p) {
        }
        try {
            ISO8601Utils.parse("2007-08-13", new ParsePosition(0));
            fail();
        } catch (ParseException p) {
        }
        try {
            ISO8601Utils.parse("20070813", new ParsePosition(0));
            fail();
        } catch (ParseException p) {
        }
    }

    public void testParseOptional() throws java.text.ParseException {
        Date d = ISO8601Utils.parse("2007-08-13T19:51Z", new ParsePosition(0));
        assertEquals(dateZeroSecondAndMillis, d);

        d = ISO8601Utils.parse("2007-08-13T1951Z", new ParsePosition(0));
        assertEquals(dateZeroSecondAndMillis, d);

        d = ISO8601Utils.parse("2007-08-13T21:51+02:00", new ParsePosition(0));
        assertEquals(dateZeroSecondAndMillis, d);
    }
}
