package org.restexpress;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * {@link RestExpressEntryPointTest} test {@link RestExpressEntryPoint}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class RestExpressEntryPointTest {

	/**
	 * The REST server that handles the test calls.
	 */
	private static RestExpressLauncher launcher;

	@BeforeClass
	public static void beforeClass() throws Exception {
		launcher = new RestExpressLauncher();
		launcher.bind();
	}

	@AfterClass
	public static void afterClass() {
		launcher.shutdown();
	}

	@Test
	public void checkFakeEntryPointIsLoaded() {
		Assert.assertEquals(Boolean.TRUE, launcher.server().context().get("TEST"));
	}
}
