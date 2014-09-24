package org.restexpress.domain;

import java.util.Date;

/**
 * {@link TimeStamped} define method to get "update date" on any object.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public interface TimeStamped {

    public Date updateAt();
}
