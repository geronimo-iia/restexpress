package org.restexpress.processor.json;

import java.util.List;

import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;
import org.restexpress.processor.AbstractProcessor;

/**
 * {@link JsonProcessor} define default supported {@link MediaType} in order of priority:
 * <ul>
 * <li>MediaType.APPLICATION_JSON with CharacterSet.UTF_8</li>
 * <li>MediaType.APPLICATION_JSON</li>
 * <li>MediaType.APPLICATION_ALL_JSON</li>
 * <li>MediaType.APPLICATION_JAVASCRIPT</li>
 * <li>MediaType.TEXT_JAVASCRIPT</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public abstract class JsonProcessor extends AbstractProcessor {
	/**
	 * Build a new instance of {@link JsonProcessor} with default
	 * {@link MediaType}.
	 */
	public JsonProcessor() {
		super(MediaType.APPLICATION_JSON.withCharset(CharacterSet.UTF_8.getCharsetName()), //
				MediaType.APPLICATION_JSON.getMime(),//
				MediaType.APPLICATION_ALL_JSON.getMime(), //
				MediaType.APPLICATION_JAVASCRIPT.getMime(),//
				MediaType.TEXT_JAVASCRIPT.getMime());
	}

	public JsonProcessor(String... mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
	}

	public JsonProcessor(List<String> mediaTypes) {
		super(mediaTypes);
	}

	public JsonProcessor(MediaType... mediaTypes) {
		super(mediaTypes);
	}

}
