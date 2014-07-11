package org.restexpress.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class DefaultExceptionMapperTest
{
	private ExceptionMapping mapping = new DefaultExceptionMapper();

	@Test
	public void shouldMapException()
	{
		mapping.map(ArrayIndexOutOfBoundsException.class, ServiceException.class);
		Throwable t = new ArrayIndexOutOfBoundsException("Important information here");
		Throwable u = mapping.getExceptionFor(t);
		assertNotNull(u);
		assertEquals("Important information here", u.getMessage());
		assertTrue(t == u.getCause());
	}

	@Test
	public void shouldMapToNull()
	{
		mapping.map(ArrayIndexOutOfBoundsException.class, ServiceException.class);
		Throwable t = new Exception("Important information here");
		Throwable u = mapping.getExceptionFor(t);
		assertNull(u);
	}
}
