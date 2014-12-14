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
package org.restexpress.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

/**
 * Tets case for {@link Settings}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class SettingsTest {

    @Test
    public void checkReadWriteJson() throws IOException {
        checkReadWrite(Settings.JSON);
    }

    @Test
    public void checkReadWriteYaml() throws IOException {
        checkReadWrite(Settings.YAML);
    }

    public void checkReadWrite(Settings settings) throws IOException {
        RestExpressSettings restExpressSettings = Settings.defaultRestExpressSettings();
        Path config = Files.createTempFile("restrexpress", "config");
        Settings.save(config, restExpressSettings, settings);
        RestExpressSettings settings2 = Settings.loadFrom(config, settings);
        assertNotNull(settings2);
        assertEquals(restExpressSettings, settings2);

        settings2.serverSettings().setIoThreadCount(2);
        Settings.save(config, settings2, settings);
        settings2 = Settings.loadFrom(config, settings);
        assertNotNull(settings2);
        assertNotEquals(restExpressSettings, settings2);
    }

   

}
