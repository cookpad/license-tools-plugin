package com.cookpad.android.licensetools

import org.junit.Test
import org.yaml.snakeyaml.Yaml

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotEquals;

import static com.cookpad.android.licensetools.LibraryInfo.*

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
        // Apache License 2.0
        assertEquals("apache2", normalizeLicense("apache license2"))
        assertEquals("apache2", normalizeLicense("Apache License2"))
        assertEquals("apache2", normalizeLicense("APACHE LICENSE2"))
        assertEquals("apache2", normalizeLicense("apache license2.0"))
        assertEquals("apache2", normalizeLicense("Apache License2.0"))
        assertEquals("apache2", normalizeLicense("Apache License 2.0"))
        assertEquals("apache2", normalizeLicense("The Apache License 2.0"))
        assertEquals("apache2", normalizeLicense("APACHE LICENSE2.0"))
        assertEquals("apache2", normalizeLicense("apachelicense2.0"))
        assertEquals("apache2", normalizeLicense("ApacheLicense2.0"))
        assertEquals("apache2", normalizeLicense("APACHELICENSE2.0"))
        assertEquals("apache2", normalizeLicense("apache2"))
        assertEquals("apache2", normalizeLicense("Apache2"))
        assertEquals("apache2", normalizeLicense("Apache2.0"))
        assertNotEquals("apache2", normalizeLicense("mit"))
        assertNotEquals("apache2", normalizeLicense("apache license1"))
        assertNotEquals("apache2", normalizeLicense("Apache License1"))
        assertNotEquals("apache2", normalizeLicense("APACHE LICENSE1"))
        assertNotEquals("apache2", normalizeLicense("apachelicense1.0"))
        assertNotEquals("apache2", normalizeLicense("ApacheLicense1.0"))
        assertNotEquals("apache2", normalizeLicense("APACHELICENSE1.0"))
        assertNotEquals("apache2", normalizeLicense("apache license1.0"))
        assertNotEquals("apache2", normalizeLicense("Apache License1.0"))
        assertNotEquals("apache2", normalizeLicense("APACHE LICENSE1.0"))

        // MIT
        assertEquals("mit", normalizeLicense("mit"))
        assertEquals("mit", normalizeLicense("Mit"))
        assertEquals("mit", normalizeLicense("MIT"))
        assertEquals("mit", normalizeLicense("The MIT License"))
        assertNotEquals("mit", normalizeLicense("apache2"))
        assertNotEquals("mit", normalizeLicense("Apache 2.0 License"))

        // BSD 3 Clauses
        assertEquals("bsd_3_clauses", normalizeLicense("BSD"))
        assertEquals("bsd_3_clauses", normalizeLicense("bsd"))
        assertEquals("bsd_3_clauses", normalizeLicense("The BSD License"))
        assertEquals("bsd_3_clauses", normalizeLicense("BSD 3 Clauses"))
        assertEquals("bsd_3_clauses", normalizeLicense("bsd_3_clauses"))
        assertEquals("bsd_3_clauses", normalizeLicense("The BSD 3-Clauses"))
        assertNotEquals("bsd_3_clauses", normalizeLicense("BSD 2 Clauses"))
        assertNotEquals("bsd_3_clauses", normalizeLicense("MIT"))


        // BSD 2 Clauses
        assertEquals("bsd_2_clauses", normalizeLicense("BSD 2 Clauses"))
        assertEquals("bsd_2_clauses", normalizeLicense("bsd_2_clauses"))
        assertEquals("bsd_2_clauses", normalizeLicense("The BSD 2 Clauses License"))
        assertNotEquals("bsd_2_clauses", normalizeLicense("BSD"))
        assertNotEquals("bsd_2_clauses", normalizeLicense("MIT"))

        // Other
        assertEquals("Other", normalizeLicense("Other"))
        assertEquals("No license found", normalizeLicense("No license found"))
    }
}
