package org.restexpress.plugin.content.resolver;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for {@link ResolverTest}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class ResolverTest {

    public ResolverTest() {
    }

    @Test
    public void checkResolverWithoutExcludedPart() {
        final Resolver resolver = new Resolver("", null);
        
        assertTrue(resolver.match("/root"));
        assertFalse(resolver.match("boot"));
        assertEquals("/root/", resolver.resolve("/root/"));
        assertEquals("/Root/", resolver.resolve("/Root/"));
        assertEquals("/root/a", resolver.resolve("/root/a"));
        assertEquals("/rOot/a", resolver.resolve("/rOot/a"));
        assertEquals("/rOot/a/b/c/d", resolver.resolve("/rOot/a/b/c/d"));
    }

    @Test
    public void checkResolverWithExcludedPart() {
        final Resolver resolver = new Resolver("/root", new String[] {"/root/b"});
        assertTrue(resolver.match("/root"));
        assertFalse(resolver.match("boot"));
        
        assertTrue(resolver.match("/root/a"));
        assertEquals("/a", resolver.resolve("/root/a"));
        assertFalse(resolver.match("/root/b"));
        assertFalse(resolver.match("/root/b/a"));
        
    }
}
