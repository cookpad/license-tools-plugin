package com.cookpad.android.licensetools

public class LibraryInfo implements Comparable<LibraryInfo> {

    String libraryName = ""

    ArtifactId artifactId;

    String filename = ""

    String year = ""

    String copyrightHolder = ""

    String notice = ""

    String license = ""

    String licenseUrl = ""

    String url = ""

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

    public String getEscapedName() {
        return name.contains(": ") ? "\"${name}\"" : name
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
            case ~/(?i).*\bapache.*1\.0.*/:
                return "apache1_0"
            case ~/(?i).*\bapache.*1.*/:
                return "apache1_1"
            case ~/(?i).*\bapache.*2.*/:
                return "apache2"
            case ~/(?i).*\bmit\b.*/:
                return "mit"
            case ~/(?i).*\bbsd\b.*\b2\b.*/:
            case ~/(?i).*\bsimplified\b.*\bbsd\b.*/:
                return "bsd_2_clauses"
            case ~/(?i).*\bbsd\b.*\b3\b.*/:
            case ~/(?i).*\brevised\b.*\bbsd\b.*/:
            case ~/(?i).*\bmodified\b.*\bbsd\b.*/:
                return "bsd_3_clauses"
            case ~/(?i).*\bbsd\b.*\b4\b.*/:
            case ~/(?i).*\boriginal\b.*\bbsd\b.*/:
                return "bsd_4_clauses"
            case ~/(?i).*\bbsd\b.*/:
                return "bsd_3_clauses"
            case ~/(?i).*\bisc\b.*/:
                return "isc"
            case ~/(?i).*\bmozilla\b.*\bpublic\b.*\b1\.0\b.*/:
            case ~/(?i).*\bmpl\b?.*\b?1\.0\b.*/:
                return "mpl1_0"
            case ~/(?i).*\bmozilla\b.*\bpublic\b.*\b1\b.*/:
            case ~/(?i).*\bmpl\b?.*\b?1\b.*/:
                return "mpl1_1"
            case ~/(?i).*\bmozilla\b.*\bpublic\b.*\b2\b.*/:
            case ~/(?i).*\bmpl\b?.*\b?2\b.*/:
                return "mpl2"
            case ~/(?i).*\bcommon\b.*\bpublic\b.*/:
            case ~/(?i).*\bcpl\b.*/:
                return "cpl1"
            case ~/(?i).*\beclipse\b.*\bpublic\b.*\b1\b.*/:
            case ~/(?i).*\bepl\b.*\b1\b.*/:
                return "epl1"
            case ~/(?i).*\bfree\b.*\bpublic\b.*/:
            case ~/(?i).*\bfpl\b.*/:
                return "fpl1"
            case ~/(?i).*\bfacebook\b.*\bplatform\b.*\blicense\b.*/:
                return "facebook_platform_license"
            case ~/(?i).*\bcc0\b.*/:
            case ~/(?i).*\bcreative\b.commons\b.*\b.*/:
                return "cc0"
            case ~/(?i).*\bcommon\b.*\bdevelopment\b.*\bdistribution\b.*/:
            case ~/(?i).*\bcddl\b.*/:
                return "cddl1"
            case ~/(?i).*\bgnu\b.*\blesser\b.*\bgeneral\b.*\bpublic\b.*2.*/:
            case ~/(?i).*\blgpl\b?.*2.*/:
                return "lgpl2_1"
            case ~/(?i).*\bgnu\b.*\blesser\b.*\bgeneral\b.*\bpublic\b.*3.*/:
            case ~/(?i).*\bgnu\b.*\blesser\b.*\bgeneral\b.*\bpublic\b.*/:
            case ~/(?i).*\blgpl\b?.*3.*/:
            case ~/(?i).*\blgpl\b.*/:
                return "lgpl3"
            case ~/(?i).*\bgnu\b.*\bgeneral\b.*\bpublic\b.*1.*/:
            case ~/(?i).*\bgpl\b?.*1.*/:
                return "gpl1"
            case ~/(?i).*\bgnu\b.*\bgeneral\b.*\bpublic\b.*2.*/:
            case ~/(?i).*\bgpl\b?.*2.*/:
                return "gpl2"
            case ~/(?i).*\bgnu\b.*\bgeneral\b.*\bpublic\b.*3.*/:
            case ~/(?i).*\bgnu\b.*\bgeneral\b.*\bpublic\b.*/:
            case ~/(?i).*\bgpl\b?.*3.*/:
            case ~/(?i).*\bgpl\b.*/:
                return "gpl3"
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
