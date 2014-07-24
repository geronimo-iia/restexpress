package org.restexpress.processor.xml;

import java.util.List;

import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;
import org.restexpress.processor.AbstractProcessor;

/**
 * {@link XmlProcessor} define default supported {@link MediaType}:
 * <ul>
 * <li>MediaType.APPLICATION_XML with CharacterSet.UTF_8</li>
 * <li>MediaType.APPLICATION_XML</li>
 * <li>MediaType.APPLICATION_ALL_XML</li>
 * <li>MediaType.TEXT_XML, with CharacterSet.UTF_8</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public abstract class XmlProcessor extends AbstractProcessor implements Aliasable {

	/**
	 * Build a new instance of {@link XmlProcessor} with default
	 * {@link MediaType}.
	 */
	public XmlProcessor() {
		super(MediaType.APPLICATION_XML.withCharset(CharacterSet.UTF_8.getCharsetName()),//
				MediaType.APPLICATION_XML.getMime(),//
				MediaType.APPLICATION_ALL_XML.getMime(), //
				MediaType.TEXT_XML.withCharset(CharacterSet.UTF_8.getCharsetName()));
	}

	public XmlProcessor(String... mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
	}

	public XmlProcessor(List<String> supportedMediaType) {
		super(supportedMediaType);
	}

	public XmlProcessor(MediaType... mediaTypes) {
		super(mediaTypes);
	}

}
