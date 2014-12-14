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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        final Resolver resolver = new Resolver("/root", new String[] { "/root/b" });
        assertTrue(resolver.match("/root"));
        assertFalse(resolver.match("boot"));

        assertTrue(resolver.match("/root/a"));
        assertEquals("/a", resolver.resolve("/root/a"));
        assertFalse(resolver.match("/root/b"));
        assertFalse(resolver.match("/root/b/a"));

    }
}
