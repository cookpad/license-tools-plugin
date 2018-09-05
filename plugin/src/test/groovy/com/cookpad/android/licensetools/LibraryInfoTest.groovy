package com.cookpad.android.licensetools

import org.junit.Test

import static com.cookpad.android.licensetools.LibraryInfo.joinWords
import static com.cookpad.android.licensetools.LibraryInfo.normalizeLicense
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotEquals
import static org.junit.Assert.assertTrue

public class LibraryInfoTest {
    @Test
    public void testJoinWords() throws Exception {
        assertEquals("foo", joinWords(["foo"]))
        assertEquals("foo and bar", joinWords(["foo", "bar"]))
        assertEquals("foo, bar, and baz", joinWords(["foo", "bar", "baz"]))
    }

    @Test
    void testNormalizeLicense() {
        // Apache License 1.0
        assertEquals("apache1_0", normalizeLicense("apache license1.0"))
        assertEquals("apache1_0", normalizeLicense("Apache License1.0"))
        assertEquals("apache1_0", normalizeLicense("Apache License 1.0"))
        assertEquals("apache1_0", normalizeLicense("The Apache License 1.0"))
        assertEquals("apache1_0", normalizeLicense("APACHE LICENSE1.0"))
        assertEquals("apache1_0", normalizeLicense("apachelicense1.0"))
        assertEquals("apache1_0", normalizeLicense("ApacheLicense1.0"))
        assertEquals("apache1_0", normalizeLicense("APACHELICENSE1.0"))
        assertEquals("apache1_0", normalizeLicense("apache1.0"))
        assertEquals("apache1_0", normalizeLicense("Apache1.0"))
        assertEquals("apache1_0", normalizeLicense("The Apache Software License, Version 1.0"))
        assertNotEquals("apache1_0", normalizeLicense("apache license2"))
        assertNotEquals("apache1_0", normalizeLicense("Apache License2"))
        assertNotEquals("apache1_0", normalizeLicense("APACHE LICENSE2"))
        assertNotEquals("apache1_0", normalizeLicense("apachelicense2.0"))
        assertNotEquals("apache1_0", normalizeLicense("ApacheLicense2.0"))
        assertNotEquals("apache1_0", normalizeLicense("APACHELICENSE2.0"))
        assertNotEquals("apache1_0", normalizeLicense("apache license2.0"))
        assertNotEquals("apache1_0", normalizeLicense("Apache License2.0"))
        assertNotEquals("apache1_0", normalizeLicense("APACHE LICENSE2.0"))

        // Apache License 1.1
        assertEquals("apache1_1", normalizeLicense("apache license1"))
        assertEquals("apache1_1", normalizeLicense("Apache License1"))
        assertEquals("apache1_1", normalizeLicense("APACHE LICENSE1"))
        assertEquals("apache1_1", normalizeLicense("apache license1.1"))
        assertEquals("apache1_1", normalizeLicense("Apache License1.1"))
        assertEquals("apache1_1", normalizeLicense("Apache License 1.1"))
        assertEquals("apache1_1", normalizeLicense("The Apache License 1.1"))
        assertEquals("apache1_1", normalizeLicense("APACHE LICENSE1.1"))
        assertEquals("apache1_1", normalizeLicense("apachelicense1.1"))
        assertEquals("apache1_1", normalizeLicense("ApacheLicense1.1"))
        assertEquals("apache1_1", normalizeLicense("APACHELICENSE1.1"))
        assertEquals("apache1_1", normalizeLicense("apache1"))
        assertEquals("apache1_1", normalizeLicense("Apache1"))
        assertEquals("apache1_1", normalizeLicense("Apache1.1"))
        assertEquals("apache1_1", normalizeLicense("The Apache Software License, Version 1.1"))
        assertNotEquals("apache1_1", normalizeLicense("apache license2"))
        assertNotEquals("apache1_1", normalizeLicense("Apache License2"))
        assertNotEquals("apache1_1", normalizeLicense("APACHE LICENSE2"))
        assertNotEquals("apache1_1", normalizeLicense("apachelicense2.0"))
        assertNotEquals("apache1_1", normalizeLicense("ApacheLicense2.0"))
        assertNotEquals("apache1_1", normalizeLicense("APACHELICENSE2.0"))
        assertNotEquals("apache1_1", normalizeLicense("apache license2.0"))
        assertNotEquals("apache1_1", normalizeLicense("Apache License2.0"))
        assertNotEquals("apache1_1", normalizeLicense("APACHE LICENSE2.0"))

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
        assertEquals("apache2", normalizeLicense("The Apache Software License, Version 2.0"))
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

        // BSD 4 Clauses
        assertEquals("bsd_4_clauses", normalizeLicense("BSD 4 Clauses"))
        assertEquals("bsd_4_clauses", normalizeLicense("bsd_4_clauses"))
        assertEquals("bsd_4_clauses", normalizeLicense("The BSD 4-Clauses"))
        assertEquals("bsd_4_clauses", normalizeLicense("Original BSD License"))
        assertNotEquals("bsd_4_clauses", normalizeLicense("BSD"))
        assertNotEquals("bsd_4_clauses", normalizeLicense("BSD 2 Clauses"))
        assertNotEquals("bsd_4_clauses", normalizeLicense("BSD 3 Clauses"))

        // BSD 3 Clauses
        assertEquals("bsd_3_clauses", normalizeLicense("BSD"))
        assertEquals("bsd_3_clauses", normalizeLicense("bsd"))
        assertEquals("bsd_3_clauses", normalizeLicense("The BSD License"))
        assertEquals("bsd_3_clauses", normalizeLicense("BSD 3 Clauses"))
        assertEquals("bsd_3_clauses", normalizeLicense("bsd_3_clauses"))
        assertEquals("bsd_3_clauses", normalizeLicense("The BSD 3-Clauses"))
        assertEquals("bsd_3_clauses", normalizeLicense("Revised BSD License"))
        assertEquals("bsd_3_clauses", normalizeLicense("Modified BSD License"))
        assertNotEquals("bsd_3_clauses", normalizeLicense("BSD 2 Clauses"))
        assertNotEquals("bsd_3_clauses", normalizeLicense("MIT"))

        // BSD 2 Clauses
        assertEquals("bsd_2_clauses", normalizeLicense("BSD 2 Clauses"))
        assertEquals("bsd_2_clauses", normalizeLicense("bsd_2_clauses"))
        assertEquals("bsd_2_clauses", normalizeLicense("The BSD 2 Clauses License"))
        assertEquals("bsd_2_clauses", normalizeLicense("Simplified BSD License"))
        assertNotEquals("bsd_2_clauses", normalizeLicense("BSD"))
        assertNotEquals("bsd_2_clauses", normalizeLicense("MIT"))

        // ISC
        assertEquals("isc", normalizeLicense("isc"))
        assertEquals("isc", normalizeLicense("Isc"))
        assertEquals("isc", normalizeLicense("ISC"))
        assertEquals("isc", normalizeLicense("The ISC License"))
        assertNotEquals("isc", normalizeLicense("Apache 2.0 License"))
        assertNotEquals("isc", normalizeLicense("MIT"))

        // MPL 1.0
        assertEquals("mpl1_0", normalizeLicense("mpl1.0"))
        assertEquals("mpl1_0", normalizeLicense("MPL 1.0"))
        assertEquals("mpl1_0", normalizeLicense("Mozilla Public License, Version 1.0"))
        assertEquals("mpl1_0", normalizeLicense("Mozilla Public License 1.0"))
        assertNotEquals("mpl1_0", normalizeLicense("mpl1"))
        assertNotEquals("mpl1_0", normalizeLicense("mpl1.1"))
        assertNotEquals("mpl1_0", normalizeLicense("mpl2"))
        assertNotEquals("mpl1_0", normalizeLicense("mpl2.0"))
        assertNotEquals("mpl1_0", normalizeLicense("Mozilla Public License 1.1"))
        assertNotEquals("mpl1_0", normalizeLicense("Mozilla Public License 2.0"))

        // MPL 1.1
        assertEquals("mpl1_1", normalizeLicense("mpl1"))
        assertEquals("mpl1_1", normalizeLicense("MPL 1.1"))
        assertEquals("mpl1_1", normalizeLicense("Mozilla Public License, Version 1.1"))
        assertEquals("mpl1_1", normalizeLicense("Mozilla Public License 1.1"))
        assertNotEquals("mpl1_1", normalizeLicense("mpl2"))
        assertNotEquals("mpl1_1", normalizeLicense("Mozilla Public License 3.0"))

        // MPL 2.0
        assertEquals("mpl2", normalizeLicense("mpl2"))
        assertEquals("mpl2", normalizeLicense("MPL 2.0"))
        assertEquals("mpl2", normalizeLicense("Mozilla Public License, Version 2.0"))
        assertEquals("mpl2", normalizeLicense("Mozilla Public License 2.0"))
        assertNotEquals("mpl2", normalizeLicense("mpl1"))
        assertNotEquals("mpl2", normalizeLicense("Mozilla Public License 3.0"))

        // CPL 1.0
        assertEquals("cpl1", normalizeLicense("cpl1"))
        assertEquals("cpl1", normalizeLicense("CPL 1.0"))
        assertEquals("cpl1", normalizeLicense("Common Public License - v 1.0"))
        assertEquals("cpl1", normalizeLicense("Common Public License, Version 1.0"))

        // EPL 1.0
        assertEquals("epl1", normalizeLicense("epl1"))
        assertEquals("epl1", normalizeLicense("EPL 1.0"))
        assertEquals("epl1", normalizeLicense("Eclipse Public License - v 1.0"))
        assertEquals("epl1", normalizeLicense("Eclipse Public License, Version 1.0"))
        assertNotEquals("epl1", normalizeLicense("epl2"))
        assertNotEquals("epl1", normalizeLicense("Eclipse Public License 3.0"))

        // FPL 1.0
        assertEquals("fpl1", normalizeLicense("fpl1"))
        assertEquals("fpl1", normalizeLicense("FPL 1.0"))
        assertEquals("fpl1", normalizeLicense("Free Public License - v 1.0"))
        assertEquals("fpl1", normalizeLicense("Free Public License, Version 1.0"))

        // Facebook Platform License
        assertEquals("facebook_platform_license", normalizeLicense("Facebook Platform License"))
        assertEquals("facebook_platform_license", normalizeLicense("facebook_platform_license"))
        assertEquals("facebook_platform_license", normalizeLicense("The Facebook Platform License"))
        assertNotEquals("facebook_platform_license", normalizeLicense("BSD"))
        assertNotEquals("facebook_platform_license", normalizeLicense("MIT"))

        // CCO 1.0
        assertEquals("cc0", normalizeLicense("CC0"))
        assertEquals("cc0", normalizeLicense("CC0 1.0"))
        assertEquals("cc0", normalizeLicense("cc0"))
        assertEquals("cc0", normalizeLicense("Creative Commons"))
        assertNotEquals("cc0", normalizeLicense("BSD"))
        assertNotEquals("cc0", normalizeLicense("MIT"))

        // CDDL 1.0
        assertEquals("cddl1", normalizeLicense("cddl1"))
        assertEquals("cddl1", normalizeLicense("CDDL 1.0"))
        assertEquals("cddl1", normalizeLicense("COMMON DEVELOPMENT AND DISTRIBUTION LICENSE - v 1.0"))
        assertEquals("cddl1", normalizeLicense("COMMON DEVELOPMENT AND DISTRIBUTION LICENSE, Version 1.0"))

        // LGPL 2.1
        assertEquals("lgpl2_1", normalizeLicense("lgpl2"))
        assertEquals("lgpl2_1", normalizeLicense("lgpl2.1"))
        assertEquals("lgpl2_1", normalizeLicense("LGPL 2"))
        assertEquals("lgpl2_1", normalizeLicense("LGPL 2.1"))
        assertEquals("lgpl2_1", normalizeLicense("GNU Lesser General Public License - v 2"))
        assertEquals("lgpl2_1", normalizeLicense("GNU Lesser General Public License - v 2.1"))
        assertEquals("lgpl2_1", normalizeLicense("GNU Lesser General Public License, Version 2"))
        assertEquals("lgpl2_1", normalizeLicense("GNU Lesser General Public License, Version 2.1"))
        assertNotEquals("lgpl2_1", normalizeLicense("lgpl3"))
        assertNotEquals("lgpl2_1", normalizeLicense("lgpl3.0"))
        assertNotEquals("lgpl2_1", normalizeLicense("GNU Lesser General Public License 3"))
        assertNotEquals("lgpl2_1", normalizeLicense("GNU Lesser General Public License 3.0"))

        // LGPL 3.0
        assertEquals("lgpl3", normalizeLicense("lgpl3"))
        assertEquals("lgpl3", normalizeLicense("lgpl3.0"))
        assertEquals("lgpl3", normalizeLicense("LGPL 3"))
        assertEquals("lgpl3", normalizeLicense("LGPL 3.0"))
        assertEquals("lgpl3", normalizeLicense("GNU Lesser General Public License - v 3"))
        assertEquals("lgpl3", normalizeLicense("GNU Lesser General Public License - v 3.0"))
        assertEquals("lgpl3", normalizeLicense("GNU Lesser General Public License, Version 3"))
        assertEquals("lgpl3", normalizeLicense("GNU Lesser General Public License, Version 3.0"))
        assertNotEquals("lgpl3", normalizeLicense("lgpl2"))
        assertNotEquals("lgpl3", normalizeLicense("lgpl2.1"))
        assertNotEquals("lgpl3", normalizeLicense("GNU Lesser General Public License 2"))
        assertNotEquals("lgpl3", normalizeLicense("GNU Lesser General Public License 2.1"))

        // GPL 1.0
        assertEquals("gpl1", normalizeLicense("gpl1"))
        assertEquals("gpl1", normalizeLicense("gpl1.0"))
        assertEquals("gpl1", normalizeLicense("GPL 1"))
        assertEquals("gpl1", normalizeLicense("GPL 1.0"))
        assertEquals("gpl1", normalizeLicense("GNU GENERAL PUBLIC LICENSE - v 1"))
        assertEquals("gpl1", normalizeLicense("GNU GENERAL PUBLIC LICENSE - v 1.0"))
        assertEquals("gpl1", normalizeLicense("GNU GENERAL PUBLIC LICENSE, Version 1"))
        assertEquals("gpl1", normalizeLicense("GNU GENERAL PUBLIC LICENSE, Version 1.0"))
        assertNotEquals("gpl1", normalizeLicense("gpl2"))
        assertNotEquals("gpl1", normalizeLicense("gpl2.0"))
        assertNotEquals("gpl1", normalizeLicense("gpl3"))
        assertNotEquals("gpl1", normalizeLicense("gpl3.0"))
        assertNotEquals("gpl1", normalizeLicense("GNU GENERAL PUBLIC LICENSE 2"))
        assertNotEquals("gpl1", normalizeLicense("GNU GENERAL PUBLIC LICENSE 2.0"))
        assertNotEquals("gpl1", normalizeLicense("GNU GENERAL PUBLIC LICENSE 3"))
        assertNotEquals("gpl1", normalizeLicense("GNU GENERAL PUBLIC LICENSE 3.0"))

        // GPL 2.0
        assertEquals("gpl2", normalizeLicense("gpl2"))
        assertEquals("gpl2", normalizeLicense("gpl2.0"))
        assertEquals("gpl2", normalizeLicense("GPL 2"))
        assertEquals("gpl2", normalizeLicense("GPL 2.0"))
        assertEquals("gpl2", normalizeLicense("GNU GENERAL PUBLIC LICENSE - v 2"))
        assertEquals("gpl2", normalizeLicense("GNU GENERAL PUBLIC LICENSE - v 2.0"))
        assertEquals("gpl2", normalizeLicense("GNU GENERAL PUBLIC LICENSE, Version 2"))
        assertEquals("gpl2", normalizeLicense("GNU GENERAL PUBLIC LICENSE, Version 2.0"))
        assertNotEquals("gpl2", normalizeLicense("gpl1"))
        assertNotEquals("gpl2", normalizeLicense("gpl1.0"))
        assertNotEquals("gpl2", normalizeLicense("gpl3"))
        assertNotEquals("gpl2", normalizeLicense("gpl3.0"))
        assertNotEquals("gpl2", normalizeLicense("GNU GENERAL PUBLIC LICENSE 1"))
        assertNotEquals("gpl2", normalizeLicense("GNU GENERAL PUBLIC LICENSE 1.0"))
        assertNotEquals("gpl2", normalizeLicense("GNU GENERAL PUBLIC LICENSE 3"))
        assertNotEquals("gpl2", normalizeLicense("GNU GENERAL PUBLIC LICENSE 3.0"))

        // GPL 3.0
        assertEquals("gpl3", normalizeLicense("gpl3"))
        assertEquals("gpl3", normalizeLicense("gpl3.0"))
        assertEquals("gpl3", normalizeLicense("GPL 3"))
        assertEquals("gpl3", normalizeLicense("GPL 3.0"))
        assertEquals("gpl3", normalizeLicense("GNU GENERAL PUBLIC LICENSE - v 3"))
        assertEquals("gpl3", normalizeLicense("GNU GENERAL PUBLIC LICENSE - v 3.0"))
        assertEquals("gpl3", normalizeLicense("GNU GENERAL PUBLIC LICENSE, Version 3"))
        assertEquals("gpl3", normalizeLicense("GNU GENERAL PUBLIC LICENSE, Version 3.0"))
        assertNotEquals("gpl3", normalizeLicense("gpl1"))
        assertNotEquals("gpl3", normalizeLicense("gpl1.0"))
        assertNotEquals("gpl3", normalizeLicense("gpl2"))
        assertNotEquals("gpl3", normalizeLicense("gpl2.0"))
        assertNotEquals("gpl3", normalizeLicense("GNU GENERAL PUBLIC LICENSE 1"))
        assertNotEquals("gpl3", normalizeLicense("GNU GENERAL PUBLIC LICENSE 1.0"))
        assertNotEquals("gpl3", normalizeLicense("GNU GENERAL PUBLIC LICENSE 2"))
        assertNotEquals("gpl3", normalizeLicense("GNU GENERAL PUBLIC LICENSE 2.0"))

        // MoPub SDK License
        assertEquals("mopub_sdk_license", normalizeLicense("MoPub"))
        assertEquals("mopub_sdk_license", normalizeLicense("MoPub SDK License"))
        assertNotEquals("mopub_sdk_license", normalizeLicense("BSD"))
        assertNotEquals("mopub_sdk_license", normalizeLicense("MIT"))

        // Other
        assertEquals("Other", normalizeLicense("Other"))
        assertEquals("No license found", normalizeLicense("No license found"))
    }

    @Test
    public void testCopyrightStatementWithEndDot() throws Exception {
        LibraryInfo libraryInfo = LibraryInfo.fromYaml([
                "copyrightHolder": "Foo Inc.",
        ])

        assertEquals("Copyright &copy; Foo Inc. All rights reserved.", libraryInfo.copyrightStatement)
    }

    @Test
    public void testCopyrightStatementWithoutEndDot() throws Exception {
        LibraryInfo libraryInfo = LibraryInfo.fromYaml([
                "copyrightHolder": "Foo",
        ])

        assertEquals("Copyright &copy; Foo. All rights reserved.", libraryInfo.copyrightStatement)
    }

    @Test
    public void testEquals() throws Exception {
        LibraryInfo libraryInfo = LibraryInfo.fromYaml([
                artifact: "com.example1:foo:1.0"
        ])

        assertEquals(libraryInfo, LibraryInfo.fromYaml([
                artifact: "com.example1:foo:1.0"
        ]))
        assertNotEquals(libraryInfo, LibraryInfo.fromYaml([
                artifact: "com.example1:foo:2.0"
        ]))
        assertNotEquals(libraryInfo, LibraryInfo.fromYaml([
                artifact: "com.example1:foo:+"
        ]))
        assertNotEquals(libraryInfo, LibraryInfo.fromYaml([
                artifact: "com.example1:bar:1.0"
        ]))
        assertNotEquals(libraryInfo, LibraryInfo.fromYaml([
                artifact: "com.example2:foo:1.0"
        ]))
    }

    @Test
    public void testCompareTo() throws Exception {
        LibraryInfo libraryInfo = LibraryInfo.fromYaml([
                artifact: "com.example1:foo:1.0"
        ])

        assertEquals(0,
                libraryInfo.compareTo(LibraryInfo.fromYaml([
                        artifact: "com.example1:foo:1.0"
                ]))
        )
        assertTrue(
                libraryInfo.compareTo(LibraryInfo.fromYaml([
                        artifact: "com.example1:foo:2.0"
                ])) < 0
        )
        assertTrue(
                libraryInfo.compareTo(LibraryInfo.fromYaml([
                        artifact: "com.example1:foo:+"
                ])) > 0
        )
        assertTrue(
                libraryInfo.compareTo(LibraryInfo.fromYaml([
                        artifact: "com.example1:bar:1.0"
                ])) > 0
        )
        assertTrue(
                libraryInfo.compareTo(LibraryInfo.fromYaml([
                        artifact: "com.example2:foo:1.0"
                ])) < 0
        )
    }

}
