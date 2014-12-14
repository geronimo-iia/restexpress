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
package org.restexpress.plugin.content;

import java.io.File;

/**
 * {@link FileRemovals} group some methods to deal with files removal. We must take care that a clean should have a finite time.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public enum FileRemovals {
    ;
    /**
     * Delete a file or directory with silent.
     * 
     * @param file
     */
    public static void delete(final File file) {
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
    public static void cleanDirectory(final File file) {
        if (file != null && file.exists())
            if (file.isDirectory()) {
                final File[] files = file.listFiles();
                if (files != null)
                    for (final File f : files)
                        delete(f);
            }
    }

}
