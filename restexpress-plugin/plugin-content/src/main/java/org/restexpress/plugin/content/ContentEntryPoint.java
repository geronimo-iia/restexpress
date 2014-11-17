package org.restexpress.plugin.content;

import org.restexpress.RestExpress;
import org.restexpress.RestExpressEntryPoint;

/**
 * {@link ContentEntryPoint} declare controller for serve all static content.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class ContentEntryPoint implements RestExpressEntryPoint {

    public ContentEntryPoint() {
        super();
    }

    @Override
    public void onLoad(RestExpress restExpress) throws RuntimeException {
        
    }

}
