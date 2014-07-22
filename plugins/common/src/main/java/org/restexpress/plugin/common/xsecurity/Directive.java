package org.restexpress.plugin.common.xsecurity;

import java.util.Locale;

/**
 * X-Secutiry Directive.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public enum Directive {
	DEFAULT, SCRIPT, OBJECT, STYLE, IMG, MEDIA, FRAME, FONT, CONNECT;

	public String toString() {
		return name().toLowerCase(Locale.US) + "-src";
	};
}
