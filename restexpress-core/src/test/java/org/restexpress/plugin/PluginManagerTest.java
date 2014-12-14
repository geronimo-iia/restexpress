package org.restexpress.plugin;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * {@link PluginManagerTest} test for {@link PluginManager}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class PluginManagerTest {

	@Test
	public void lookupPluginByInterface() {
		PluginManager pluginManager = new PluginManager();
		pluginManager.register(new PluginA());
		pluginManager.register(new PluginB());
		assertTrue(pluginManager.find(DummyPluginInterface.class) != null);
	}

	@Test
	public void lookupPluginByName() {
		PluginManager pluginManager = new PluginManager();
		assertTrue(pluginManager.find("PluginB") == null);
		pluginManager.register(new PluginB());
		assertTrue(pluginManager.find("PluginB") != null);
	}

}
