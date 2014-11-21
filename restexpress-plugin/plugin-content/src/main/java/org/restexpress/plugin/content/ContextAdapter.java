package org.restexpress.plugin.content;

import java.io.File;
import java.io.IOException;

public interface ContextAdapter {

    /**
     * @param name
     * @return {@link Boolean.TRUE} if named resource can be retrieved by this {@link ContextAdapter}.
     */
    public Boolean match(final String name);

    /**
     * Retrieve a file.
     * 
     * @param name
     * @return {@link File} instance or null if not found or an error occurs
     * @throws IOException
     */
    public File retrieve(final String name) throws IOException;

}
