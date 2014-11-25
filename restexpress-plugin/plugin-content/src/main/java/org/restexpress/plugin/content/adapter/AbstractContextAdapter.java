package org.restexpress.plugin.content.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.restexpress.plugin.content.ContextAdapter;
import org.restexpress.plugin.content.resolver.Resolver;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closer;

/**
 * {@link AbstractContextAdapter} implement default behavior for all {@link ContextAdapter} which deal with temporary directory, remote
 * document base, and context retrieval.
 * 
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public abstract class AbstractContextAdapter<T> implements ContextAdapter {

    /**
     * Slash constant.
     */
    private static final String SLASH = "/";

    /**
     * {@link Resolver}.
     */
    protected final Resolver resolver;

    /**
     * Remote document base.
     */
    protected final String remoteDocumentBase;

    /**
     * Temporary directory.
     */
    protected final File tempDirectory;

    /**
     * Build a new instance of {@link WebRemoteClient}.
     * 
     * @param resolver resolver instance.
     * @param remoteDocumentBase document base associated with this adapter.
     * @param tempDirectory temporary directory
     * @throws NullPointerException if resolver, remoteDocumentBase or tempDirectory is null
     */
    public AbstractContextAdapter(Resolver resolver, final String remoteDocumentBase, final File tempDirectory) {
        super();
        this.remoteDocumentBase = Preconditions.checkNotNull(remoteDocumentBase);
        this.resolver = Preconditions.checkNotNull(resolver);
        this.tempDirectory = Preconditions.checkNotNull(tempDirectory);
    }

    public Boolean match(final String name) {
        return resolver.match(name);
    }

    @Override
    public File retrieve(final String name) throws IOException {
        if (name != null) {
            // extract remote name
            final String remoteName = resolver.resolve(name);
            if (remoteName != null) {
                // extract remote path
                final String remotePath = resolveRemotePath(remoteName, Boolean.TRUE);
                T context = null;
                try {
                    // create context
                    context = createContext();
                    return retrieveFile(context, name, remotePath);
                } finally {
                    // destroy context
                    destroyContext(context);
                }
            }
        }
        return null;
    }

    /**
     * Retrieve specified resource name associated with the given remote path.
     * 
     * @param context current context
     * @param name resource name
     * @param remotePath remote path
     * @return a {@link File} resource or null if not exists.
     * @throws IOException
     */
    protected File retrieveFile(final T context, final String name, final String remotePath) throws IOException {
        // test if exist
        if (exists(context, remotePath)) {
            File target = getLocalFile(name);
            // build local file system
            final File parent = target.getParentFile();
            if (parent != null)
                parent.mkdirs();
            // copy file
            final Closer closer = Closer.create();
            try {
                // get a copy.
                final InputStream in = closer.register(get(context, remotePath));
                final OutputStream out = closer.register(new FileOutputStream(target));
                ByteStreams.copy(in, out);
                out.flush();
            } catch (final Throwable e) {
                if (target != null)
                    target.delete();
                target = null;
            } finally {
                closer.close();
            }
            return target;
        }
        return null;
    }

    /**
     * Create context.
     * 
     * @return
     */
    protected abstract T createContext();

    /**
     * Destroy context.
     * 
     * @param context
     */
    protected abstract void destroyContext(T context);

    /**
     * @param remotePath
     * @return {@link Boolean.TRUE} if remote path exists on remote host.
     */
    protected abstract boolean exists(T context, String remotePath) throws IOException;

    /**
     * @param remotePath
     * @return an InputStream onto specified remote path.
     */
    protected abstract InputStream get(T context, String remotePath) throws IOException;

    /**
     * Get local file from target name.
     * 
     * @param name
     * @return a {@link File} instance.
     */
    protected final File getLocalFile(final String name) {
        return !name.isEmpty() && name.charAt(0) == '/' ? new File(tempDirectory, name.substring(1)) : new File(tempDirectory, name);
    }

    /**
     * Build a remote path from docBase and parameters.
     * 
     * @param name name of file
     * @param asFile true if name should map a file, or a directory if false..
     * @return a path.
     */
    protected final String resolveRemotePath(final String remoteName, final boolean asFile) {
        final StringBuilder builder = new StringBuilder(remoteDocumentBase);

        if (!remoteName.isEmpty()) {
            // did remote start with '/' ?
            if (remoteName.charAt(0) != '/')
                builder.append(SLASH);

            // add remote part
            builder.append(remoteName);
        }

        // remove '/' ending
        final int len = builder.length();
        if (builder.charAt(len - 1) == '/')
            builder.deleteCharAt(len - 1);

        // if directory add '/'
        if (!asFile)
            builder.append('/');

        return builder.toString();
    }

}
