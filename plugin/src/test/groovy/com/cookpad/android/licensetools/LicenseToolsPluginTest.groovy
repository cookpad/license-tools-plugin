package com.cookpad.android.licensetools

import org.junit.Test
import org.yaml.snakeyaml.Yaml

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotEquals;

public class LicenseToolsPluginTest {

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
        LicenseToolsPlugin tool = new LicenseToolsPlugin();
        // Apache License 2.0
        assertEquals("apache2", tool.normalizeLicense("apache license2"))
        assertEquals("apache2", tool.normalizeLicense("Apache License2"))
        assertEquals("apache2", tool.normalizeLicense("APACHE LICENSE2"))
        assertEquals("apache2", tool.normalizeLicense("apache license2.0"))
        assertEquals("apache2", tool.normalizeLicense("Apache License2.0"))
        assertEquals("apache2", tool.normalizeLicense("Apache License 2.0"))
        assertEquals("apache2", tool.normalizeLicense("The Apache License 2.0"))
        assertEquals("apache2", tool.normalizeLicense("APACHE LICENSE2.0"))
        assertEquals("apache2", tool.normalizeLicense("apachelicense2.0"))
        assertEquals("apache2", tool.normalizeLicense("ApacheLicense2.0"))
        assertEquals("apache2", tool.normalizeLicense("APACHELICENSE2.0"))
        assertEquals("apache2", tool.normalizeLicense("apache2"))
        assertEquals("apache2", tool.normalizeLicense("Apache2"))
        assertEquals("apache2", tool.normalizeLicense("Apache2.0"))
        assertNotEquals("apache2", tool.normalizeLicense("mit"))
        assertNotEquals("apache2", tool.normalizeLicense("apache license1"))
        assertNotEquals("apache2", tool.normalizeLicense("Apache License1"))
        assertNotEquals("apache2", tool.normalizeLicense("APACHE LICENSE1"))
        assertNotEquals("apache2", tool.normalizeLicense("apachelicense1.0"))
        assertNotEquals("apache2", tool.normalizeLicense("ApacheLicense1.0"))
        assertNotEquals("apache2", tool.normalizeLicense("APACHELICENSE1.0"))
        assertNotEquals("apache2", tool.normalizeLicense("apache license1.0"))
        assertNotEquals("apache2", tool.normalizeLicense("Apache License1.0"))
        assertNotEquals("apache2", tool.normalizeLicense("APACHE LICENSE1.0"))

        // MIT
        assertEquals("mit", tool.normalizeLicense("mit"))
        assertEquals("mit", tool.normalizeLicense("Mit"))
        assertEquals("mit", tool.normalizeLicense("MIT"))
        assertEquals("mit", tool.normalizeLicense("The MIT License"))
        assertNotEquals("mit", tool.normalizeLicense("apache2"))
        assertNotEquals("mit", tool.normalizeLicense("Apache 2.0 License"))

        // BSD 3 Clauses
        assertEquals("bsd_3_clauses", tool.normalizeLicense("BSD"))
        assertEquals("bsd_3_clauses", tool.normalizeLicense("bsd"))
        assertEquals("bsd_3_clauses", tool.normalizeLicense("The BSD License"))
        assertEquals("bsd_3_clauses", tool.normalizeLicense("BSD 3 Clauses"))
        assertEquals("bsd_3_clauses", tool.normalizeLicense("bsd_3_clauses"))
        assertEquals("bsd_3_clauses", tool.normalizeLicense("The BSD 3-Clauses"))
        assertNotEquals("bsd_3_clauses", tool.normalizeLicense("BSD 2 Clauses"))
        assertNotEquals("bsd_3_clauses", tool.normalizeLicense("MIT"))


        // BSD 2 Clauses
        assertEquals("bsd_2_clauses", tool.normalizeLicense("BSD 2 Clauses"))
        assertEquals("bsd_2_clauses", tool.normalizeLicense("bsd_2_clauses"))
        assertEquals("bsd_2_clauses", tool.normalizeLicense("The BSD 2 Clauses License"))
        assertNotEquals("bsd_2_clauses", tool.normalizeLicense("BSD"))
        assertNotEquals("bsd_2_clauses", tool.normalizeLicense("MIT"))

        // Other
        assertEquals("Other", tool.normalizeLicense("Other"))
        assertEquals("No license found", tool.normalizeLicense("No license found"))
    }
}
