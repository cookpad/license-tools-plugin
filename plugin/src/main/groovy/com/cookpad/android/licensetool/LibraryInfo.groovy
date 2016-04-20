package com.cookpad.android.licensetool

public class LibraryInfo {

    String libraryName = "#libraryName#"

    String artifactId = "#artifactId#"

    String filename = "#filename#"

    int year = 0;

    List<String> authors = []

    String notice = "#notice#"

    String license = "apache2"

    public String getName() {
        return libraryName ?: artifactId ?: filename
    }

    // called from HTML templates
    public String getCopyrightStatement() {
        if (notice) {
            return notice;
        } else if (authors.size() == 1) {
            return buildCopyrightStatement(authors.first())
        } else {
            assert authors.size() > 1

            // example: a, b, c, and d
            String last = authors.last()
            return  buildCopyrightStatement(authors.subList(0, authors.size() - 2).join(", ") + ", and " + last)
        }
    }

    String buildCopyrightStatement(String authors) {
        if (year > 0) {
            return "Copyright &copy; ${year}, ${authors}. All rights reserved."
        } else {
            return "Copyright &copy; ${authors}. All rights reserved."
        }
    }

}
