package com.cookpad.android.licensetools

import org.junit.Test
import org.yaml.snakeyaml.Yaml

import static com.cookpad.android.licensetools.LibraryInfo.joinWords

class LicenseToolsPluginTest {
    private static def YAML = new Yaml()
    private static def EMPTY_ARTIFACT = new ArtifactId("", "", "")

    private static Map<String, ?>[] loadAsYaml(String text) {
        return YAML.load(text) as Map<String, ?>[] ?: []
    }

    def targets = [
            LibraryInfo.fromYaml(
                    artifact: "com.cookpad.android:licensetools:+",
                    filename: "filename",
                    year: "2000",
                    copyrightHolder: joinWords(["Person1", "Person2", "Person3"]),
                    notice: "NOTICE",
                    license: "Sample License",
                    licenseUrl: "https://github.com/cookpad/license-tools-plugin/LICENSE",
                    url: "https://github.com/cookpad/license-tools-plugin",
            ),
            LibraryInfo.fromYaml( // contains : with spaces
                    libraryName: "Cookpad License : Tool",
                    artifactId: "com.cookpad.android:licensetools:+",
                    filename: "filename",
                    year: "2000",
                    copyrightHolder: joinWords(["Person1", "Person2", "Person3"]),
                    notice: "NOTICE",
                    license: "Sample License",
                    licenseUrl: "https://github.com/cookpad/license-tools-plugin/LICENSE",
                    url: "https://github.com/cookpad/license-tools-plugin",
            ),
            LibraryInfo.fromYaml( // licenseUrl is missing
                    libraryName: "Cookpad License Tool",
                    artifactId: "com.cookpad.android:licensetools:+",
                    filename: "filename",
                    year: "2000",
                    copyrightHolder: joinWords(["Person1", "Person2", "Person3"]),
                    notice: "NOTICE",
                    license: "Sample License",
                    url: "https://github.com/cookpad/license-tools-plugin",
            ),
            LibraryInfo.fromYaml( // url is missing
                    libraryName: "Cookpad License Tool",
                    artifactId: "com.cookpad.android:licensetools:+",
                    filename: "filename",
                    year: "2000",
                    copyrightHolder: joinWords(["Person1", "Person2", "Person3"]),
                    notice: "NOTICE",
                    license: "Sample License",
                    licenseUrl: "https://github.com/cookpad/license-tools-plugin/LICENSE",
            ),
            LibraryInfo.fromYaml( // copyrightHolder is missing
                    libraryName: "Cookpad License Tool",
                    artifactId: "com.cookpad.android:licensetools:+",
                    filename: "filename",
                    year: "2000",
                    notice: "NOTICE",
                    license: "Sample License",
                    licenseUrl: "https://github.com/cookpad/license-tools-plugin/LICENSE",
                    url: "https://github.com/cookpad/license-tools-plugin",
            ),
            LibraryInfo.fromYaml( // copyrightHolder is empty
                    libraryName: "Cookpad License Tool",
                    artifactId: "com.cookpad.android:licensetools:+",
                    filename: "filename",
                    year: "2000",
                    copyrightHolder: "",
                    notice: "NOTICE",
                    license: "Sample License",
                    licenseUrl: "https://github.com/cookpad/license-tools-plugin/LICENSE",
                    url: "https://github.com/cookpad/license-tools-plugin",
            ),
            LibraryInfo.fromYaml( // license is missing
                    libraryName: "Cookpad License Tool",
                    artifactId: "com.cookpad.android:licensetools:+",
                    filename: "filename",
                    year: "2000",
                    copyrightHolder: joinWords(["Person1", "Person2", "Person3"]),
                    notice: "NOTICE",
                    licenseUrl: "https://github.com/cookpad/license-tools-plugin/LICENSE",
                    url: "https://github.com/cookpad/license-tools-plugin",
            ),
            LibraryInfo.fromYaml( // libraryName is missing
                    artifactId: "com.cookpad.android:licensetools:+",
                    filename: "filename",
                    year: "2000",
                    copyrightHolder: joinWords(["Person1", "Person2", "Person3"]),
                    notice: "NOTICE",
                    license: "Sample License",
                    licenseUrl: "https://github.com/cookpad/license-tools-plugin/LICENSE",
                    url: "https://github.com/cookpad/license-tools-plugin",
            ),
            LibraryInfo.fromYaml( // use filename as a fallback
                    filename: "filename",
                    year: "2000",
                    copyrightHolder: joinWords(["Person1", "Person2", "Person3"]),
                    notice: "NOTICE",
                    license: "Sample License",
                    licenseUrl: "https://github.com/cookpad/license-tools-plugin/LICENSE",
                    url: "https://github.com/cookpad/license-tools-plugin",
            ),
            LibraryInfo.fromYaml( // name stuff is missing
                    year: "2000",
                    copyrightHolder: joinWords(["Person1", "Person2", "Person3"]),
                    notice: "NOTICE",
                    license: "Sample License",
                    licenseUrl: "https://github.com/cookpad/license-tools-plugin/LICENSE",
                    url: "https://github.com/cookpad/license-tools-plugin",
            ),
            LibraryInfo.fromYaml( // notice is missing
                    artifact: "com.cookpad.android:licensetools:+",
                    filename: "filename",
                    year: "2000",
                    copyrightHolder: joinWords(["Person1", "Person2", "Person3"]),
                    license: "Sample License",
                    licenseUrl: "https://github.com/cookpad/license-tools-plugin/LICENSE",
                    url: "https://github.com/cookpad/license-tools-plugin",
            ),
            LibraryInfo.fromYaml( // copyright stuff is missing
                    artifact: "com.cookpad.android:licensetools:+",
                    filename: "filename",
                    year: "2000",
                    license: "Sample License",
                    licenseUrl: "https://github.com/cookpad/license-tools-plugin/LICENSE",
                    url: "https://github.com/cookpad/license-tools-plugin",
            ),
    ]

    @Test
    void testGenerateLibraryInfoText() {
        for (target in targets) {
            def text = LicenseToolsPlugin.generateLibraryInfoText(target)
            LibraryInfo actual = LibraryInfo.fromYaml(loadAsYaml(text)[0])

            assert target.artifactId.withWildcardVersion() == actual.artifactId.withWildcardVersion()
            assert (target.name ?: "#NAME#") == actual.name
            assert (target.copyrightHolder ?: "#COPYRIGHT_HOLDER#") == actual.copyrightHolder
            assert (target.license ?: "#LICENSE#") == actual.license

            if (target.licenseUrl) {
                assert (target.licenseUrl ?: "#LICENSEURL#") == actual.licenseUrl
            } else {
                assert actual.licenseUrl == ""
            }

            if (target.url) {
                assert (target.url ?: "#URL#") == actual.url
            } else {
                assert actual.url == null
            }
        }
    }

    @Test
    void testGenerateMissingLibraryInfoText() {
        for (target in targets) {
            if (target.artifactId == EMPTY_ARTIFACT) {
                // missing library must have artifact id
                continue
            }

            def text = LicenseToolsPlugin.generateMissingLibraryInfoText(target)
            LibraryInfo actual = LibraryInfo.fromYaml(loadAsYaml(text)[0])

            assert target.artifactId == actual.artifactId
            assert target.name == actual.name

            if (target.copyrightStatement) {
                assert actual.copyrightStatement == null
            } else {
                assert "#AUTHOR# (or authors: [...])" == actual.copyrightHolder
                assert "#YEAR# (optional)" == actual.year
            }

            if (target.license) {
                assert actual.license == ""
            } else {
                assert "#LICENSE#" == actual.license
            }
        }
    }
}
