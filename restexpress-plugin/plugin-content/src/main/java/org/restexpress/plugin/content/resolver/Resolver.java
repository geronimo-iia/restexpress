package org.restexpress.plugin.content.resolver;

/**
 * {@link Resolver} manage exclusion and resolve path management.
 * 
 */
public class Resolver {

    /**
     * {@link PathResolver}.
     */
    protected final PathResolver pathResolver;

    /**
     * {@link ExclusionResolver}.
     */
    protected final ExclusionResolver exclusionResolver;

    /**
     * Build a new instance of {@link WebRemoteClient}.
     * 
     * @param path location associated with this remote ('/' or empty string stand for root).
     * @param excludedPath array of excluded path.
     * @throws NullPointerException if path is null
     */
    public Resolver(final String path, final String[] excludedPath) {
        super();
        pathResolver = new PathResolver(path);
        exclusionResolver = new ExclusionResolver(excludedPath);
    }

    /**
     * @param name
     * @return {@link Boolean.TRUE} if named resource is not excluded and match path.
     */
    public Boolean match(final String name) {
        return !exclusionResolver.isExcluded(name) && pathResolver.match(name);
    }

    /**
     * Extract remote name from specified name by removing configured path.
     * 
     * Assert: name must start with path and not null ({@link Resolver#match(String)} == {@link Boolean#TRUE}).
     * 
     * @param name
     * @return remote name or null.
     */
    public String resolve(String name) {
        return pathResolver.resolve(name);
    }

    @Override
    public String toString() {
        return "Resolver [pathResolver=" + pathResolver + ", exclusionResolver=" + exclusionResolver + "]";
    }

}
