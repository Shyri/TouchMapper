package es.shyri.touchmapper.output.config;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ConfigParsingTest {
    @Test
    public void testReadFile() throws IOException {
        ConfigParser parser = new ConfigParser();
        TouchConfig touchConfig = parser.parseConfig(getFilePath("samples/simple_mapping.json"));
    }

    private File getFilePath(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(path);
        return new File(resource.getPath());
    }
}
