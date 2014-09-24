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
package org.restexpress.time;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.intelligentsia.commons.http.HttpHeaderDateTimeFormat;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class TimeTest {

    @Test
    public void checkParseFormat() throws ParseException {
        final Date date = HttpHeaderDateTimeFormat.parseAny("2010-12-17T12:00:00Z");
        System.err.println("internal:");
        System.err.println(HttpHeaderDateTimeFormat.ISO_8601.format(date));
        // ok with Joda, but maybe too strict
        System.err.println("joda:");
        DateTimeFormatter parser    = ISODateTimeFormat.dateOptionalTimeParser().withZoneUTC();
        DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis().withZoneUTC();
        DateTime dateTimeHere     = parser.parseDateTime("2010-12-17T12:00:00Z");
        System.err.println(formatter.print(dateTimeHere));
        
        
        System.err.println("joda2:");
        DateTimeFormatter parser2    = ISODateTimeFormat.basicDateTimeNoMillis().withZoneUTC();
        dateTimeHere     = parser2.parseDateTime("20101217T090000Z");
        System.err.println(formatter.print(dateTimeHere));
        
        System.err.println("Jackson:");
        ISO8601DateFormat df = new ISO8601DateFormat();
        Date d = df.parse("2010-12-17T01:00:00Z");
        System.err.println(df.format(d));
        
        d = df.parse("20101217T090000Z");
        System.err.println(df.format(d));
        
    }
    
    @Test
    public void testTimeZone() {
        DateTimeFormatter parser    = ISODateTimeFormat.dateTimeParser();
        DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis();

        DateTime dateTimeHere     = parser.parseDateTime("2012-01-19T19:00:00-05:00");

        DateTime dateTimeInLondon = dateTimeHere.withZone(DateTimeZone.forID("Europe/London"));
        DateTime dateTimeInParis  = dateTimeHere.withZone(DateTimeZone.forID("Europe/Paris"));

        assertEquals("2012-01-20T00:00:00Z", formatter.print(dateTimeInLondon));
        assertEquals("2012-01-20T01:00:00+01:00", formatter.print(dateTimeInParis));
    }
    
    
    @Test
    public void testiso() throws ParseException {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());

        System.out.println(nowAsISO);

        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        //nowAsISO = "2013-05-31T00:00:00Z";
        Date finalResult = df1.parse(nowAsISO);

        System.out.println(finalResult);
        
    }
}
