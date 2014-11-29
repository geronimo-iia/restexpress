package org.restexpress.domain;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.restexpress.domain.Format;

import static org.junit.Assert.*;

/**
 * {@link Format} test case.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class FormatTest {

	@Test
	public void checkToMap() {
		Map<String, String> formats = Format.toMap();
		assertNotNull(formats);
		assertEquals(Format.JSON.getMediaType(), formats.get(Format.JSON.getExtension()));
	}

	@Test
	public void checkAsMap() {
		assertNotNull(Format.asMap());
		assertEquals(Format.JSON.getMediaType(), Format.asMap().get(Format.JSON.getExtension()));
		assertEquals(Format.XML.getMediaType(), Format.asMap().get(Format.XML.getExtension()));
	}

	@Test
	public void checkValueForExtension() {
		assertNull(Format.valueForExtension("IDONTEXIST"));
		assertEquals(Format.JSON, Format.valueForExtension(Format.JSON.getExtension()));
	}

	@Test(expected = NullPointerException.class)
	public void checkNullValuesForMediaType() {
		assertNotNull(Format.valuesForMediaType((String) null));
		assertTrue(Format.valuesForMediaType((String) null).isEmpty());
		Format.valuesForMediaType((Set<String>) null);
	}

	@Test
	public void checkNullValueForMediaType() {
		assertNull(Format.valueForMediaType(null));
	}

	@Test
	public void checkValueForMediaTypeAgainstValuesForMediaType() {
		for (Format format : Format.values()) {
			assertEquals(Format.valuesForMediaType(format.getMediaType()).get(0), Format.valueForMediaType(format.getMediaType()));
		}
	}

	@Test
	public void checkConfiguration() {
		for (Format format : Format.values()) {
			assertNotNull(format.getExtension());
			assertNotNull(format.getMediaType());
		}
	}
}
