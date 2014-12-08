package org.restexpress.processor;

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.restexpress.Response;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.util.HttpSpecification;

/**
 * {@link DefaultContentTypePostprocessor} implement a {@link Postprocessor}
 * which add default content type if none was provided AND if content is
 * allowed.
 * 
 * Default Content Type is per default "text/plain; utf-8".
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public final class DefaultContentTypePostprocessor implements Postprocessor {

	private final String defaultContentType;

	/**
	 * Build a new instance of {@link DefaultContentTypePostprocessor} with
	 * specified default content type.
	 * 
	 * @param defaultContentType
	 *            default Content Type to use.
	 */
	public DefaultContentTypePostprocessor(String defaultContentType) {
		super();
		this.defaultContentType = defaultContentType;
	}

	/**
	 * Build a new instance of {@link DefaultContentTypePostprocessor} with
	 * default content type "text/plain; utf-8".
	 */
	public DefaultContentTypePostprocessor() {
		this(MediaType.TEXT_PLAIN.withCharset(CharacterSet.UTF_8.getCharsetName()));
	}

	@Override
	public void process(MessageContext context) {
		final Response response = context.getResponse();
		if (HttpSpecification.isContentTypeAllowed(response)) {
			if (!response.hasHeader(HttpHeaders.Names.CONTENT_TYPE)) {
				response.setContentType(defaultContentType);
			}
		}
	}

	@Override
	public String toString() {
		return "DefaultContentTypePostprocessor [defaultContentType=" + defaultContentType + "]";
	}

}
