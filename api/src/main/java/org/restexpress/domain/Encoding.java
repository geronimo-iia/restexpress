package org.restexpress.domain;

/**
 * Encoding.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public enum Encoding {
	/** All s acceptable. */
	ALL("*"),

	/** The common Unix file compression. */
	COMPRESS("compress"),

	/** The zlib format defined by RFC 1950 and 1951. */
	DEFLATE("deflate"),

	/** The zlib format defined by RFC 1950 and 1951, without wrapping. */
	DEFLATE_NOWRAP("deflate-no-wrap"),

	/** The FreeMarker . */
	FREEMARKER("freemarker"),

	/** The GNU Zip . */
	GZIP("gzip"),

	/** The default (identity)  */
	IDENTITY("identity"),

	/** The Velocity . */
	VELOCITY("velocity"),

	/** The Info-Zip . */
	ZIP("zip");

	private final String value;

	private Encoding(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
}
