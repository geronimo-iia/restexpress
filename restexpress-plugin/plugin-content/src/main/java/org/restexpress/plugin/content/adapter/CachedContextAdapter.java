/**
 * 
 */
package org.restexpress.plugin.content.adapter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.restexpress.plugin.content.ContextAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.util.concurrent.UncheckedExecutionException;

/**
 * {@link CachedContextAdapter} implements a Cached {@link ContextAdapter} using underlying GUAVA framework.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class CachedContextAdapter implements ContextAdapter {
    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(CachedContextAdapter.class);

    /**
     * Delegated instance.
     */
    protected ContextAdapter delegate;

    /**
     * File Cache
     */
    protected final LoadingCache<String, Optional<File>> fileCache;

    /**
     * Temporary directory.
     */
    protected final File tempDirectory;

    /**
     * Build a new instance of {@link CachedContextAdapter}.
     * 
     * @param delegate
     * @param tempDirectory
     */
    public CachedContextAdapter(final ContextAdapter delegate, final File tempDirectory) {
        this(delegate, tempDirectory, 3000, 25000, 10);
    }

    /**
     * Build a new instance of {@link CachedContextAdapter}.
     * 
     * @param delegate
     * @param tempDirectory
     * @param initialCapacity
     * @param maximumSize
     * @param expireAfterWrite
     */
    public CachedContextAdapter(final ContextAdapter delegate, final File tempDirectory, final int initialCapacity,
            final int maximumSize, final int expireAfterWrite) {
        super();
        this.tempDirectory = Preconditions.checkNotNull(tempDirectory);
        this.delegate = Preconditions.checkNotNull(delegate);

        fileCache = CacheBuilder.newBuilder() //
                .initialCapacity(initialCapacity)//
                .maximumSize(maximumSize)//
                .concurrencyLevel(12)//
                .expireAfterWrite(expireAfterWrite, TimeUnit.MINUTES) // for demo only
                .removalListener(new RemovalListener<String, Optional<File>>() {

                    @Override
                    public void onRemoval(final RemovalNotification<String, Optional<File>> notification) {
                        // do not clean up directory
                        if (!notification.getKey().endsWith("/") && tempDirectory != null && notification.getValue().isPresent())
                            delete(notification.getValue().get());
                    }

                }).build(new CacheLoader<String, Optional<File>>() {

                    @Override
                    public Optional<File> load(final String name) throws Exception {
                        return Optional.fromNullable(delegate.retrieve(name));
                    }
                });
    }

    @Override
    public File retrieve(String name) throws IOException {
        File result = null;
        if (name != null)
            try {
                final Optional<File> value = fileCache.get(name);
                if (value.isPresent()) {
                    result = value.get();
                    if (result == null)
                        // Should not occurs
                        fileCache.invalidate(name);
                    else // file from cache has be removed by hand
                    if (!result.exists()) {
                        fileCache.invalidate(name);
                        result = null;
                    }
                }
            } catch (final UncheckedExecutionException u) {
                log.error(u.getMessage());
                u.printStackTrace();
            } catch (final ExecutionException e) {
                log.error(e.getMessage());
            }
        return result;
    }

    @Override
    public Boolean match(String name) {
        return delegate.match(name);
    }

    /**
     * Delete a file or directory with silent.
     * 
     * @param file
     */
    protected static void delete(final File file) {
        if (file.isDirectory())
            cleanDirectory(file);
        try {
            file.delete();
        } catch (final Throwable throwable) {
            try {
                file.deleteOnExit();
            } catch (final Throwable th) {
                // log.warn(file.getPath() + " could not be cleaned");
            }
        }
    }

    /**
     * Clean a directory
     * 
     * @param file
     */
    protected static void cleanDirectory(final File file) {
        if (file != null && file.exists())
            if (file.isDirectory()) {
                final File[] files = file.listFiles();
                if (files != null)
                    for (final File f : files)
                        delete(f);
            }
    }

}
