package org.restexpress.plugin.content.adapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.restexpress.plugin.content.ContextAdapter;
import org.restexpress.plugin.content.resolver.Resolver;

/**
 * {@link ClassPathContextAdapter} implement a {@link ContextAdapter} which retrieve resource from class path.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class ClassPathContextAdapter extends AbstractContextAdapter<ClassLoader> {

    public ClassPathContextAdapter(Resolver resolver, String remoteDocumentBase, File tempDirectory) {
        super(resolver, remoteDocumentBase, tempDirectory);
    }

    @Override
    protected ClassLoader createContext() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    protected void destroyContext(ClassLoader context) {
        // nothing to do
    }

    @Override
    protected boolean exists(ClassLoader context, String remotePath) throws IOException {
        return context.getResource(remotePath) != null;
    }

    @Override
    protected InputStream get(ClassLoader context, String remotePath) throws IOException {
        return context.getResourceAsStream(remotePath);
    }

    public class ClassPathContext {
        ClassLoader classLoader;
    }
}
