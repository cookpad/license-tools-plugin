package com.cookpad.android.licensetools

public class LibraryInfo implements Comparable<LibraryInfo> {

    String libraryName = ""

    ArtifactId artifactId;

    String filename = ""

    String year = "";

    String copyrightHolder = "";

    String notice = ""

    String license = ""

    String licenseUrl = ""

    String url = "";

    boolean skip = false

    // from libraries.yml
    public static LibraryInfo fromYaml(Object lib) {
        def libraryInfo = new LibraryInfo()
        libraryInfo.artifactId = ArtifactId.parse(lib.artifact as String)
        libraryInfo.filename = lib.filename as String
        libraryInfo.year = lib.year as String
        libraryInfo.libraryName = lib.name as String
        if (lib.copyrightHolder) {
            libraryInfo.copyrightHolder = lib.copyrightHolder
        } else if (lib.copyrightHolders) {
            libraryInfo.copyrightHolder = joinWords(lib.copyrightHolders)
        } else if (lib.authors) {
            libraryInfo.copyrightHolder = joinWords(lib.authors)
        } else if (lib.author) {
            libraryInfo.copyrightHolder = lib.author
        }
        libraryInfo.license = lib.license ?: ""
        libraryInfo.licenseUrl = lib.licenseUrl ?: ""
        libraryInfo.notice = lib.notice as String
        libraryInfo.skip = lib.skip as boolean
        libraryInfo.url = lib.url as String
        return libraryInfo
    }

    private String getNameFromArtifactId() {
        if (artifactId) {
            if (artifactId.name != "+") {
                return artifactId.name
            } else {
                return artifactId.group
            }
        } else {
            return null
        }
    }

    public String getName() {
        return libraryName ?: getNameFromArtifactId() ?: filename
    }

    private String getId() {
        return artifactId ?: filename
    }

    // called from HTML templates
    public String getCopyrightStatement() {
        if (notice) {
            return notice;
        } else if (!copyrightHolder) {
            return null;
        } else {
            return buildCopyrightStatement(copyrightHolder)
        }
    }

    private String buildCopyrightStatement(String copyrightHolder) {
        def dot = copyrightHolder.endsWith(".") ? "" : "."
        if (year) {
            return "Copyright &copy; ${year}, ${copyrightHolder}${dot} All rights reserved."
        } else {
            return "Copyright &copy; ${copyrightHolder}${dot} All rights reserved."
        }
    }

    public String getNormalizedLicense() {
        return normalizeLicense(license ?: "")
    }

    static String normalizeLicense(String name) {
        switch (name) {
            case ~/(?i).*\bapache.*2.*/:
                return "apache2"
            case ~/(?i).*\bmit\b.*/:
                return "mit"
            case ~/(?i).*\bbsd\b.*\b2\b.*/:
            case ~/(?i).*\bsimplified\b.*\bbsd\b.*/:
                return "bsd_2_clauses"
            case ~/(?i).*\bbsd\b.*\b3\b.*/:
                return "bsd_3_clauses"
            case ~/(?i).*\bbsd\b.*\b4\b.*/:
                return "bsd_4_clauses" // not supported because it is a very legacy license
            case ~/(?i).*\bbsd\b.*/:
                return "bsd_3_clauses"
            case ~/(?i).*\bisc\b.*/:
                return "isc"
            default:
                return name
        }
    }

    static String joinWords(List<String> words) {
        if (words.size() == 0) {
            return ""
        } else if (words.size() == 1) {
            return words.first()
        } else if (words.size() == 2) {
            return "${words.first()} and ${words.last()}"
        } else {
            // example: a, b, c, and d
            String last = words.last()
            return words.subList(0, words.size() - 1).join(", ") + ", and " + last
        }
    }

    boolean equals(o) {
        if (this.is(o)) {
            return true
        }
        if (getClass() != o.class) {
            return false
        }

        LibraryInfo that = (LibraryInfo) o

        if (id != that.id) {
            return false
        }

        return true
    }

    int hashCode() {
        return id.hashCode()
    }

    @Override
    int compareTo(LibraryInfo o) {
        return id.compareToIgnoreCase(o.id)
    }
}
