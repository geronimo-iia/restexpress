package org.restexpress.domain;

/**
 * {@link MediaType} define few MIME type.
 * 
 * @see http://en.wikipedia.org/wiki/MIME
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public enum MediaType {

	ALL("*/*"), //
	APPLICATION_ALL("application/*"), //
	APPLICATION_ALL_JSON("application/*+json"), //
	APPLICATION_ALL_XML("application/*+xml"), //
	APPLICATION_JSON("application/json"), //
	APPLICATION_XML("application/xml"), //
	APPLICATION_HAL_JSON("application/hal+json"), //
	APPLICATION_HAL_XML("application/hal+xml"), //
	TEXT_ALL("text/*"), //
	TEXT_XML("text/xml"), //
	TEXT_JAVASCRIPT("text/javascript"), //
	TEXT_PLAIN("text/plain"), //
	TEXT_HTML("text/html"), //
	TEXT_CSS("text/css")//
	;
	private final String mime;

	private MediaType(String mime) {
		this.mime = mime;
	}

	public String getMime() {
		return mime;
	}

}
