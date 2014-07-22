package org.restexpress.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@link ServerContext} class maintains a Map of name/value pairs.
 * 
 * <p>
 * It's a way for passing data from different sources to lower levels in the
 * framework.
 * </p>
 * 
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class ServerContext {

	private final transient Map<String, Object> environment;

	/**
	 * Build a new instance of {@link ServerContext}.
	 */
	public ServerContext() {
		environment = new ConcurrentHashMap<>();
	}

	public boolean containsKey(Object key) {
		return environment.containsKey(key);
	}

	public Object get(String name) {
		return environment.get(name);
	}

	public Object put(String name, Object value) {
		return environment.put(name, value);
	}

	public Object remove(String name) {
		return environment.remove(name);
	}

	public void clear() {
		environment.clear();
	}

}
