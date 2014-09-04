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
        DateTimeFormatter parser2    = ISODateTimeFormat.basicDateTime().withZoneUTC();
        dateTimeHere     = parser2.parseDateTime("2010-12-17T120000Z");
        System.err.println(formatter.print(dateTimeHere));
        
        System.err.println("Jackson:");
        ISO8601DateFormat df = new ISO8601DateFormat();
        Date d = df.parse("2010-12-17T12:00:00Z");
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
