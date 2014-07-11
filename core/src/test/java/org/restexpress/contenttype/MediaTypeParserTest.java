/*
    Copyright 2013, Strategic Gains, Inc.

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
package org.restexpress.contenttype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.restexpress.contenttype.MediaRange;
import org.restexpress.contenttype.MediaTypeParser;

/**
 * @author toddf
 * @since Jan 18, 2013
 */
public class MediaTypeParserTest
{
	@Test
	public void shouldParseQFactor()
	{
		List<MediaRange> r = MediaTypeParser.parse("text/plain; q=0.5, text/html, text/x-dvi; q=0.8, text/x-c");
		assertNotNull(r);
		assertEquals(4, r.size());

		MediaRange m1 = r.get(0);
		assertEquals("text/plain", m1.asMediaType());
		assertEquals(0.5, m1.qvalue, 0.01);
		assertTrue(m1.parameters.isEmpty());

		MediaRange m2 = r.get(1);
		assertEquals("text/html", m2.asMediaType());
		assertEquals(1.0, m2.qvalue, 0.01);
		assertTrue(m2.parameters.isEmpty());
		
		MediaRange m3 = r.get(2);
		assertEquals("text/x-dvi", m3.asMediaType());
		assertEquals(0.8, m3.qvalue, 0.01);
		assertTrue(m3.parameters.isEmpty());
		
		MediaRange m4 = r.get(3);
		assertEquals("text/x-c", m4.asMediaType());
		assertEquals(1.0, m4.qvalue, 0.01);
		assertTrue(m4.parameters.isEmpty());
	}

	@Test
	public void shouldParameters()
	{
		List<MediaRange> r = MediaTypeParser.parse("text/*, text/html, text/html;level=1, */*");
		assertNotNull(r);
		assertEquals(4, r.size());

		MediaRange m1 = r.get(0);
		assertEquals("text/*", m1.asMediaType());
		assertEquals(1.0, m1.qvalue, 0.01);
		assertTrue(m1.parameters.isEmpty());

		MediaRange m2 = r.get(1);
		assertEquals("text/html", m2.asMediaType());
		assertEquals(1.0, m2.qvalue, 0.01);
		assertTrue(m2.parameters.isEmpty());
		
		MediaRange m3 = r.get(2);
		assertEquals("text/html; level=1", m3.asMediaType());
		assertEquals(1.0, m3.qvalue, 0.01);
		assertEquals(1, m3.parameters.size());
		assertEquals("1", m3.parameters.get("level"));
		
		MediaRange m4 = r.get(3);
		assertEquals("*/*", m4.asMediaType());
		assertEquals(1.0, m4.qvalue, 0.01);
		assertTrue(m4.parameters.isEmpty());
	}

	@Test
	public void shouldParseParametersAndQFactor()
	{
		List<MediaRange> r = MediaTypeParser.parse("text/*;q=0.3 , text/html;q=0.7, text/html;q=0.9;level=1,text/html;level=2;q=0.4, */*;q=0.5");
		assertNotNull(r);
		assertEquals(5, r.size());

		MediaRange m1 = r.get(0);
		assertEquals("text/*", m1.asMediaType());
		assertEquals(0.3, m1.qvalue, 0.01);
		assertTrue(m1.parameters.isEmpty());

		MediaRange m2 = r.get(1);
		assertEquals("text/html", m2.asMediaType());
		assertEquals(0.7, m2.qvalue, 0.01);
		assertTrue(m2.parameters.isEmpty());
		
		MediaRange m3 = r.get(2);
		assertEquals("text/html; level=1", m3.asMediaType());
		assertEquals(0.9, m3.qvalue, 0.01);
		assertEquals(1, m3.parameters.size());
		assertEquals("1", m3.parameters.get("level"));
		
		MediaRange m4 = r.get(3);
		assertEquals("text/html; level=2", m4.asMediaType());
		assertEquals(0.4, m4.qvalue, 0.01);
		assertEquals("2", m4.parameters.get("level"));
		
		MediaRange m5 = r.get(4);
		assertEquals("*/*", m5.asMediaType());
		assertEquals(0.5, m5.qvalue, 0.01);
		assertTrue(m5.parameters.isEmpty());
	}

	@Test
	public void shouldChooseTextXml()
	{
		List<MediaRange> supported = MediaTypeParser.parse("application/xbel+xml, text/xml");
		List<MediaRange> requested = MediaTypeParser.parse("text/*;q=0.5,*; q=0.1");
		assertEquals("text/xml", MediaTypeParser.getBestMatch(supported, requested));
	}

	@Test
	public void shouldChooseTextJson()
	{
		List<MediaRange> supported = MediaTypeParser.parse("application/json, text/json");
		List<MediaRange> requested = MediaTypeParser.parse("image/*;q=0.5,text/*; q=0.1");
		assertEquals("text/json", MediaTypeParser.getBestMatch(supported, requested));
	}

	@Test
	public void shouldChooseTextHtml()
	{
		List<MediaRange> supported = MediaTypeParser.parse("text/plain; q=0.5, text/html, text/x-dvi; q=0.8, text/x-c");
		List<MediaRange> requested = MediaTypeParser.parse("text/html;q=0.5,text/plain; q=0.1,*/*");
		assertEquals("text/html", MediaTypeParser.getBestMatch(supported, requested));
	}

	@Test
	public void shouldChooseTextXC()
	{
		List<MediaRange> supported = MediaTypeParser.parse("text/plain; q=0.5, text/html, text/x-dvi; q=0.8, text/x-c");
		List<MediaRange> requested = MediaTypeParser.parse("*/*;q=0.9, text/x-c");
		assertEquals("text/x-c", MediaTypeParser.getBestMatch(supported, requested));
	}

	@Test
	public void shouldChooseLevel1()
	{
		List<MediaRange> supported = MediaTypeParser.parse("text/*;q=0.3 , text/html;q=0.7, text/html;q=0.9;level=1,text/html;level=2;q=0.4, */*;q=0.5");
		List<MediaRange> requested = MediaTypeParser.parse("text/*");
		assertEquals("text/html; level=1", MediaTypeParser.getBestMatch(supported, requested));
	}

	@Test
	public void shouldChooseApplicationJson()
	{
		List<MediaRange> supported = MediaTypeParser.parse("application/json, application/javascript, text/javascript, application/xml, text/xml");
		List<MediaRange> requested = MediaTypeParser.parse("application/json; application/xml");
		assertEquals("application/json", MediaTypeParser.getBestMatch(supported, requested));
	}

	@Test
	public void shouldChooseApplicationJsonWithApplicationStar()
	{
		List<MediaRange> supported = MediaTypeParser.parse("application/json, application/javascript, text/javascript, application/xml, text/xml");
		List<MediaRange> requested = MediaTypeParser.parse("application/*; text/xml");
		assertEquals("application/json", MediaTypeParser.getBestMatch(supported, requested));
	}

	@Test
	public void shouldChooseApplicationXml()
	{
		List<MediaRange> supported = MediaTypeParser.parse("application/json, application/javascript, text/javascript, application/xml, text/xml");
		List<MediaRange> requested = MediaTypeParser.parse("application/xml;application/json");
		assertEquals("application/xml", MediaTypeParser.getBestMatch(supported, requested));
	}

	@Test
	public void shouldChooseTextJavascriptWithTextStar()
	{
		List<MediaRange> supported = MediaTypeParser.parse("application/json, application/javascript, text/javascript, application/xml, text/xml");
		List<MediaRange> requested = MediaTypeParser.parse("text/*; text/xml");
		assertEquals("text/javascript", MediaTypeParser.getBestMatch(supported, requested));
	}

	@Test
	public void shouldChooseApplicationJsonWithStarStar()
	{
		List<MediaRange> supported = MediaTypeParser.parse("text/javascript;q=0.8, application/json, application/xml, text/xml");
		List<MediaRange> requested = MediaTypeParser.parse("*/*");
		assertEquals("application/json", MediaTypeParser.getBestMatch(supported, requested));
	}

	@Test
	public void shouldReturnNull()
	{
		List<MediaRange> supported = MediaTypeParser.parse("text/javascript, application/json, application/xml, text/xml");
		List<MediaRange> requested = MediaTypeParser.parse("application/blah, text/plain");
		assertNull(MediaTypeParser.getBestMatch(supported, requested));
	}
}
