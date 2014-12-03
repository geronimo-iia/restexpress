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
