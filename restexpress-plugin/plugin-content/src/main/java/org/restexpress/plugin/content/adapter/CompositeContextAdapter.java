package org.restexpress.plugin.content.adapter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.restexpress.plugin.content.ContextAdapter;

import com.google.common.base.Preconditions;

/**
 * {@link CompositeContextAdapter} implement a composite {@link ContextAdapter}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class CompositeContextAdapter implements ContextAdapter, Iterable<ContextAdapter> {

    protected ContextAdapter[] clients;

    private int size;

    /**
     * Build a new instance of {@link CompositeContextAdapter}.
     * 
     * @param clients list of {@link ContextAdapter} client to add
     * @throws NullPointerException if {@code clients} is null
     * @throws IllegalArgumentException if {@code clients} is empty
     */
    public CompositeContextAdapter(final List<ContextAdapter> clients) {
        super();
        Preconditions.checkArgument(Preconditions.checkNotNull(clients).size() > 0);
        this.clients = clients.toArray(new AbstractContextAdapter<?>[clients.size()]);
        size = this.clients.length;
    }

    /**
     * Build a new instance of {@link CompositeContextAdapter}.
     * 
     * @param clients array of {@link ContextAdapter} client to add
     * @throws NullPointerException if {@code clients} is null
     * @throws IllegalArgumentException if {@code clients} is empty
     */
    public CompositeContextAdapter(final ContextAdapter[] clients) {
        super();
        Preconditions.checkArgument(Preconditions.checkNotNull(clients).length > 0);
        this.clients = clients;
        size = this.clients.length;
    }

    @Override
    public Boolean match(String name) {
        int i = 0;
        boolean result = false;
        do {
            result = clients[i].match(name);
            i++;
        } while (!result && i < size);
        return result;
    }

    @Override
    public File retrieve(final String name) throws IOException {
        int i = 0;
        File result = null;
        do {
            if (clients[i].match(name))
                result = clients[i].retrieve(name);
            i++;
        } while (result == null && i < size);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [clients=" + Arrays.toString(clients) + "]";
    }

    @Override
    public Iterator<ContextAdapter> iterator() {
        return Arrays.asList(clients).iterator();
    }
}
