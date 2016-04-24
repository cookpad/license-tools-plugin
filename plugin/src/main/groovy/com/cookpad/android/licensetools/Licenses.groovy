package com.cookpad.android.licensetools

import groovy.text.SimpleTemplateEngine
import groovy.transform.CompileStatic

import java.util.zip.ZipFile

@CompileStatic
public class Licenses {

    static final SimpleTemplateEngine templateEngine = new SimpleTemplateEngine()

    public static String buildHtml(LibraryInfo library) {
        if (!library.license) {
            throw new NotEnoughInformationException(library)
        }
        if (!library.copyrightStatement) {
            throw new NotEnoughInformationException(library)
        }

        def templateFile = "template/${library.normalizedLicense}.html"
        return templateEngine.createTemplate(readResourceContent(templateFile)).make([
                "library": library
        ])
    }

    static String readResourceContent(String filename) {
        def templateFileUrl = Licenses.class.getClassLoader().getResource(filename)
        if (templateFileUrl == null) {
            throw new FileNotFoundException("File not found: $filename")
        }
        templateFileUrl = new URL(templateFileUrl.toString())

        try {
            return templateFileUrl.openStream().getText("UTF-8")
        } catch (FileNotFoundException e) {
            // fallback to read JAR directly
            URI jarFile = (templateFileUrl.openConnection() as JarURLConnection).jarFileURL.toURI()
            ZipFile zip
            try {
                zip = new ZipFile(new File(jarFile))
            } catch (FileNotFoundException ex) {
                System.err.println("[plugin] no plugin.jar. run `./gradlew plugin:jar` first.")
                throw ex
            }
            return zip.getInputStream((zip.getEntry(filename))).getText("UTF-8")
        }
    }
}
