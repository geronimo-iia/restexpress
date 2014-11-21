package org.restexpress.plugin.content.adapter;

import java.io.File;
import java.io.IOException;

import org.restexpress.plugin.content.ContextAdapter;
import org.restexpress.plugin.content.resolver.Resolver;

public class FileContextAdapter implements ContextAdapter {

    /**
     * {@link Resolver}.
     */
    protected final Resolver resolver;

    /**
     * {@link File} as root directory
     */
    protected final File rootDirectory;

    public FileContextAdapter(Resolver resolver, File rootDirectory) {
        super();
        this.resolver = resolver;
        this.rootDirectory = rootDirectory;
    }

    @Override
    public Boolean match(String name) {
        return resolver.match(name);
    }

    @Override
    public File retrieve(String name) throws IOException {
        // extract path name
        String pathname = resolver.resolve(name);
        // locate file under root directory
        File file = new File(rootDirectory, pathname);
        // return file if exists
        return file.exists() ? file : null;
    }

}
