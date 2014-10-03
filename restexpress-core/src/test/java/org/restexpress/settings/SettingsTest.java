package org.restexpress.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

/**
 * Tets case for {@link Settings}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class SettingsTest {

    @Test
    public void checkReadWriteJson() throws IOException {
        RestExpressSettings settings = Settings.defaultRestExpressSettings();
        Path config = Files.createTempFile("restrexpress", "config");
        Settings.save(config, settings);
        RestExpressSettings settings2 = Settings.loadFromJson(config);
        assertNotNull(settings2);
        assertEquals(settings, settings2);

        settings2.serverSettings().setIoThreadCount(2);
        Settings.save(config, settings2);
        settings2 = Settings.loadFromJson(config);
        assertNotNull(settings2);
        assertNotEquals(settings, settings2);
    }

    @Test
    public void checkReadWriteYaml() {
        RestExpressSettings settings = Settings.defaultRestExpressSettings();
        Yaml yaml = Settings.getYaml();
        Writer writer = new StringWriter();
        yaml.dump(settings, writer);
        System.err.println(writer.toString());
        RestExpressSettings settings2 = (RestExpressSettings) yaml.load(writer.toString());

        assertNotNull(settings2);
        assertEquals(settings, settings2);

        settings2.serverSettings().setIoThreadCount(2);
        writer = new StringWriter();
        yaml.dump(settings2, writer);
        settings2 = (RestExpressSettings) yaml.load(writer.toString());
        assertNotNull(settings2);
        assertNotEquals(settings, settings2);
    }

}
