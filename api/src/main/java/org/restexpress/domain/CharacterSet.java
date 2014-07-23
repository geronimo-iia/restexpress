package org.restexpress.domain;

import java.nio.charset.Charset;

/**
 * {@link CharacterSet} enumerate a small subset of {@link Charset} used in
 * RestExpress.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public enum CharacterSet {
	/**
	 * @see http://en.wikipedia.org/wiki/UTF-8
	 */
	UTF_8("UTF-8"),
	/**
	 * @see http://en.wikipedia.org/wiki/UTF-16
	 */
	UTF_16("UTF-16"),
	/**
	 * @see http://en.wikipedia.org/wiki/ISO_8859-1
	 */
	ISO_8859_1("ISO-8859-1");

	private final Charset charset;

	private CharacterSet(String name) {
		this.charset = Charset.forName(name);
	}

	public Charset getCharset() {
		return charset;
	}
}
