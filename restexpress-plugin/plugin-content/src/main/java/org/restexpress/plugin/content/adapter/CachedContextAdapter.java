/**
 * 
 */
package org.restexpress.plugin.content.adapter;

import java.io.File;
import java.io.IOException;

import org.restexpress.plugin.content.ContextAdapter;

public class CachedContextAdapter implements ContextAdapter {

    
    private ContextAdapter delegate;
    
    
    public CachedContextAdapter() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public File retrieve(String name) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean match(String name) {
        // TODO Auto-generated method stub
        return null;
    }

}
