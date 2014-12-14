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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test case for {@link ExclusionResolver}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class ExclusionResolverTest {

    @Test
    public void checkNotFailOnNullParameter() {
        new ExclusionResolver(null);
    }

    @Test
    public void checkNoExclusionOnNullParameter() {
        final ExclusionResolver exclusionResolver = new ExclusionResolver(null);
        assertFalse(exclusionResolver.isExcluded(null));
        assertFalse(exclusionResolver.isExcluded(""));
        assertFalse(exclusionResolver.isExcluded("/WEB-INF/classes"));
        assertFalse(exclusionResolver.isExcluded("/WEB-INF/classes/haricot"));
        assertFalse(exclusionResolver.isExcluded("/WeB-INF/classes"));
    }

    @Test
    public void checkExcludeSinglePath() {
        final ExclusionResolver exclusionResolver = new ExclusionResolver(new String[] { "/WEB-INF/classes" });

        assertFalse(exclusionResolver.isExcluded(null));
        assertFalse(exclusionResolver.isExcluded(""));
        assertTrue(exclusionResolver.isExcluded("/WEB-INF/classes"));
        assertTrue(exclusionResolver.isExcluded("/WEB-INF/classes/haricot"));
        assertFalse(exclusionResolver.isExcluded("/WeB-INF/classes"));

    }

    @Test
    public void checkExcludeMultiplePath() {
        final ExclusionResolver exclusionResolver = new ExclusionResolver(new String[] { "/WEB-INF/classes", "/bugs" });

        assertFalse(exclusionResolver.isExcluded(null));
        assertFalse(exclusionResolver.isExcluded(""));
        assertTrue(exclusionResolver.isExcluded("/WEB-INF/classes"));
        assertTrue(exclusionResolver.isExcluded("/WEB-INF/classes/haricot"));
        assertFalse(exclusionResolver.isExcluded("/WeB-INF/classes"));
        assertTrue(exclusionResolver.isExcluded("/bugs"));
    }
}
