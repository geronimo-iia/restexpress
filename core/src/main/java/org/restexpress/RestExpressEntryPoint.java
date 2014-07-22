package org.restexpress;


/**
 * {@link RestExpressEntryPoint} declare methods to automatically process
 * something when the artifact is loaded in memory. The artifact must implements
 * this interface, and put full class name in file:
 * <code>META-INF/services/org.restexpress.RestExpressEntryPoint</code>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public interface RestExpressEntryPoint {

	/**
	 * The entry point method, called automatically by loading an artifact that
	 * declares an implementing class as an entry point.
	 * 
	 * @param restExpress
	 *            {@link RestExpress} instance.
	 * @throws RuntimeException
	 */
	public void onLoad(RestExpress restExpress) throws RuntimeException;
}
