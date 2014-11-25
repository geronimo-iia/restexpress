package org.restexpress.plugin.content;

import java.io.File;

import org.restexpress.Request;
import org.restexpress.Response;

/**
 * {@link ContentController} declare controller for serve all static content.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class ContentController {

    /**
     * Default value entry point.
     */
    public final static String DEFAULT_ENTRYPOINT= "/static";
    
    /**
     * Controller main entry point
     */
    private final String entryPoint;
    
    /**
     * Context adapter to retrieve resource.
     */
    private ContextAdapter contextAdapter;
    
    
    public ContentController() {
        this(DEFAULT_ENTRYPOINT);
    }

    public ContentController(String entryPoint) {
        super();
        this.entryPoint = entryPoint;
    }
    

    public File read(Request request, Response response) {
        return null;
    }

}
