package com.cookpad.android.licensetool

import static org.junit.Assert.*;
import org.yaml.snakeyaml.Yaml

import org.junit.Test;

public class LicenseToolTest {

    public static final Yaml yaml = new Yaml()

    @Test
    public void testSnakeYaml() {
        def input = '''
        - 1
        - apple
        - orange
        '''
        Yaml yaml = new Yaml()
        assert yaml.load(input) == [1, 'apple', 'orange']
    }

    @Test
    void testNormalizeLicense() {
        LicenseTool tool = new LicenseTool();
        // Apache License 2.0
        assertEquals("Apache License 2.0", tool.normalizeLicense("apache license2"))
        assertEquals("Apache License 2.0", tool.normalizeLicense("Apache License2"))
        assertEquals("Apache License 2.0", tool.normalizeLicense("APACHE LICENSE2"))
        assertEquals("Apache License 2.0", tool.normalizeLicense("apache license2.0"))
        assertEquals("Apache License 2.0", tool.normalizeLicense("Apache License2.0"))
        assertEquals("Apache License 2.0", tool.normalizeLicense("APACHE LICENSE2.0"))
        assertEquals("Apache License 2.0", tool.normalizeLicense("apachelicense2.0"))
        assertEquals("Apache License 2.0", tool.normalizeLicense("ApacheLicense2.0"))
        assertEquals("Apache License 2.0", tool.normalizeLicense("APACHELICENSE2.0"))
        assertEquals("Apache License 2.0", tool.normalizeLicense("apache2"))
        assertEquals("Apache License 2.0", tool.normalizeLicense("Apache2"))
        assertEquals("Apache License 2.0", tool.normalizeLicense("Apache2.0"))
        assertNotEquals("Apache License 2.0", tool.normalizeLicense("mit"))
        assertNotEquals("Apache License 2.0", tool.normalizeLicense("apache license1"))
        assertNotEquals("Apache License 2.0", tool.normalizeLicense("Apache License1"))
        assertNotEquals("Apache License 2.0", tool.normalizeLicense("APACHE LICENSE1"))
        assertNotEquals("Apache License 2.0", tool.normalizeLicense("apachelicense1.0"))
        assertNotEquals("Apache License 2.0", tool.normalizeLicense("ApacheLicense1.0"))
        assertNotEquals("Apache License 2.0", tool.normalizeLicense("APACHELICENSE1.0"))
        assertNotEquals("Apache License 2.0", tool.normalizeLicense("apache license1.0"))
        assertNotEquals("Apache License 2.0", tool.normalizeLicense("Apache License1.0"))
        assertNotEquals("Apache License 2.0", tool.normalizeLicense("APACHE LICENSE1.0"))

        // MIT
        assertEquals("MIT", tool.normalizeLicense("mit"))
        assertEquals("MIT", tool.normalizeLicense("Mit"))
        assertEquals("MIT", tool.normalizeLicense("MIT"))
        assertNotEquals("MIT", tool.normalizeLicense("apache2"))
        assertNotEquals("MIT", tool.normalizeLicense("apache2.0"))

        // Other
        assertEquals("Other", tool.normalizeLicense("Other"))
        assertEquals("No license found", tool.normalizeLicense("No license found"))
    }
}
