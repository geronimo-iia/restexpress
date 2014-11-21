package org.restexpress.plugin.content;

/**
 * {@link ContentController} declare controller for serve all static content.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class ContentController {

    public final static String DEFAULT_ENTRYPOINT= "/static";
    
//    private final String entryPoint;
    
    public ContentController() {
        this(DEFAULT_ENTRYPOINT);
    }

    public ContentController(String entryPoint) {
        super();
//        this.entryPoint = entryPoint;
    }

}
