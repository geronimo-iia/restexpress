package org.restexpress;

import java.util.Set;

import org.restexpress.domain.Format;
import org.restexpress.processor.Processor;
import org.restexpress.processor.xml.Aliasable;
import org.restexpress.response.ResponseProcessorManager;
import org.restexpress.response.ResponseWrapper;

/**
 * {@link SerializationProvider} define methods to manage serialization
 * configuration.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public interface SerializationProvider extends Aliasable {

	/**
	 * Add the specified {@link Processor} and {@link ResponseWrapper}.
	 * 
	 * @param org
	 *            .restexpress.processor
	 * @param responseWrapper
	 * @return this {@link ResponseProcessorManager} instance.
	 */
	public abstract SerializationProvider add(Processor processor, ResponseWrapper responseWrapper);

	/**
	 * Add the specified {@link Processor} and {@link ResponseWrapper}.
	 * 
	 * @param org
	 *            .restexpress.processor
	 * @param responseWrapper
	 * @param isDefault
	 * @return this {@link ResponseProcessorManager} instance.
	 */
	public abstract SerializationProvider add(Processor processor, ResponseWrapper responseWrapper, boolean isDefault);

	/**
	 * @return a {@link Set} of supported {@link Format}.
	 */
	public abstract Set<Format> supportedFormat();

	/**
	 * @param mimeType
	 * @return {@link Processor} instance for specified mime type or null if not
	 *         exists.
	 */
	public abstract Processor processor(String mimeType);

	/**
	 * @return default {@link Processor}.
	 */
	public abstract Processor defaultProcessor();

	/**
	 * @return a {@link Set} of supported media type.
	 */
	public Set<String> supportedMediaType();
}