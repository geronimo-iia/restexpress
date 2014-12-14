/**
 *        Licensed to the Apache Software Foundation (ASF) under one
 *        or more contributor license agreements.  See the NOTICE file
 *        distributed with this work for additional information
 *        regarding copyright ownership.  The ASF licenses this file
 *        to you under the Apache License, Version 2.0 (the
 *        "License"); you may not use this file except in compliance
 *        with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *        Unless required by applicable law or agreed to in writing,
 *        software distributed under the License is distributed on an
 *        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *        KIND, either express or implied.  See the License for the
 *        specific language governing permissions and limitations
 *        under the License.
 *
 */
package org.restexpress.plugin.content.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Test class for {@link PathResolver}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class PathResolverTest {

    @Test
    public void checkNullPathResolution() {
        final PathResolver resolver = new PathResolver(null);
        assertEquals("/root/", resolver.resolve("/root/"));
        assertEquals("/Root/", resolver.resolve("/Root/"));
        assertEquals("/root/a", resolver.resolve("/root/a"));
        assertEquals("/rOot/a", resolver.resolve("/rOot/a"));
        assertEquals("/rOot/a/b/c/d", resolver.resolve("/rOot/a/b/c/d"));
    }

    @Test
    public void checkRootPathResolution() {
        final PathResolver resolver = new PathResolver("/");
        assertEquals("/root/", resolver.resolve("/root/"));
        assertEquals("/Root/", resolver.resolve("/Root/"));
        assertEquals("/root/a", resolver.resolve("/root/a"));
        assertEquals("/rOot/a", resolver.resolve("/rOot/a"));
        assertEquals("/rOot/a/b/c/d", resolver.resolve("/rOot/a/b/c/d"));
    }

    @Test
    public void checkEmptyPathResolution() {
        final PathResolver resolver = new PathResolver("");
        assertEquals("/root/", resolver.resolve("/root/"));
        assertEquals("/Root/", resolver.resolve("/Root/"));
        assertEquals("/root/a", resolver.resolve("/root/a"));
        assertEquals("/rOot/a", resolver.resolve("/rOot/a"));
        assertEquals("/rOot/a/b/c/d", resolver.resolve("/rOot/a/b/c/d"));
    }

    @Test
    public void checkPathResolution() {
        final PathResolver resolver = new PathResolver("/root");
        assertEquals("/", resolver.resolve("/root/"));
        assertEquals("/", resolver.resolve("/Root/"));
        assertEquals("/a", resolver.resolve("/root/a"));
        assertEquals("/a", resolver.resolve("/rOot/a"));
        assertEquals("/a/b/c/d", resolver.resolve("/rOot/a/b/c/d"));
    }

    @Test
    public void checkOptimizedResolution() {
        final PathResolver resolver = new PathResolver("/root");
        assertEquals("t/", resolver.resolve("/pouet/"));
        assertEquals("t/a", resolver.resolve("/pouet/a"));
        assertEquals("tpouet/a/b/c/d", resolver.resolve("/pouetpouet/a/b/c/d"));
    }

    @Test
    public void checkUnderSized() {
        final PathResolver resolver = new PathResolver("/root");
        assertNull(resolver.resolve("/a"));
    }

    @Test
    public void checkNullPath() {
        assertEquals("/", new PathResolver(null).getPath());
    }

    @Test
    public void checkEmptyPath() {
        assertEquals("/", new PathResolver("").getPath());
    }

    @Test
    public void checkRootPath() {
        assertEquals("/", new PathResolver("/").getPath());
    }

    @Test
    public void checkComputedPathLen() {
        assertEquals(0, new PathResolver("/").pathLength);
    }

    @Test
    public void checkMathNull() {
        assertEquals(Boolean.FALSE, new PathResolver("/").match(null));
    }

    @Test
    public void checkMathRoot() {
        assertEquals(Boolean.TRUE, new PathResolver("/").match("/aa"));
        assertEquals(Boolean.FALSE, new PathResolver("/").match("aa"));
    }

    @Test
    public void checkMath() {
        assertEquals(Boolean.FALSE, new PathResolver("/b").match("/aa"));
        assertEquals(Boolean.TRUE, new PathResolver("/b").match("/b"));
        assertEquals(Boolean.TRUE, new PathResolver("/b").match("/b/"));
        assertEquals(Boolean.TRUE, new PathResolver("/b").match("/b/aaa"));
        assertEquals(Boolean.FALSE, new PathResolver("/b").match("/B/"));
    }
}
