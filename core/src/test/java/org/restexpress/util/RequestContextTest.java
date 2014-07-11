package org.restexpress.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RequestContextTest
{
	@Before
	public void setup()
	{
		RequestContext.clear();
	}

	@After
	public void teardown()
	{
		RequestContext.clear();
	}

	@Test
	public void shouldPut()
	{
		RequestContext.put("key", "some value");
		assertEquals("some value", RequestContext.get("key"));
		assertEquals(1, RequestContext.getContext().size());
	}

	@Test
	public void shouldClearAfterLastRemove()
	{
		RequestContext.remove("key");
		assertNull(RequestContext.getContext());
	}
}
