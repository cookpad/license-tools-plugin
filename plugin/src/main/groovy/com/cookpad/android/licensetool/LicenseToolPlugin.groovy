package com.cookpad.android.licensetool

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.yaml.snakeyaml.Yaml

class LicenseToolPlugin implements Plugin<Project> {

    final yaml = new Yaml()

    final Map<String, LibraryInfo> librariesMap = [:] // based on libraries.yml
    final Map<String, LibraryInfo> dependenciesMap = [:] // based on license plugin's dependency-license.xml

    @Override
    void apply(Project project) {
        project.extensions.add(LicenseToolExtension.NAME, LicenseToolExtension)

        def checkLicenses = project.task('checkLicenses') << {
            setup(project)

            def notDocumented = getNotDocumented()
            def notUsed = getNotUsed()
            if (notDocumented.size() == 0 && notUsed.size() == 0) {
                project.logger.info("checkLicenses: OK")
                return
            }
            if (notDocumented.size() > 0) {
                project.logger.warn("# Libraries Not Documented: ")
                notDocumented.each { key, value ->
                    def libraryInfo = dependenciesMap[key]
                    project.logger.warn("- filename: ${key} \n  license: ${libraryInfo.license}")
                }
                project.logger.warn("Check and Update libraries.yaml")
            }
            if (notUsed.size() > 0) {
                project.logger.warn("# Libraries Not Used:")
                notUsed.each { key, libraryInfo ->
                    project.logger.warn("- filename: ${key} \n  license: ${libraryInfo.license}")
                }
                project.logger.warn("Check and Update aliases.yaml")
            }
            throw new GradleException("checkLicenses failed")
        }
        project.task('checkLicense').dependsOn(checkLicenses)


        project.task('generateLicensePage') << {
            setup(project)
            generateLicensePage(project)
        }
    }

    void setup(Project project) {
        LicenseToolExtension ext = project.extensions.findByType(LicenseToolExtension)
        def aliases = loadYaml(project.file(ext.licenseAliasesYaml))
        def libraries = loadYaml(project.file(ext.licensesYaml))
        for (lib in libraries) {
            def filename = lib.filename as String
            def libraryInfo = new LibraryInfo()
            libraryInfo.filename = filename
            libraryInfo.year = lib.year
            libraryInfo.libraryName = lib.name
            libraryInfo.authors = lib.authors ?: (lib.author ? [lib.author as String] : [])
            libraryInfo.license = normalizeLicense(lib.license)
            libraryInfo.notice = lib.notice
            librariesMap[filename] = libraryInfo
        }
        setupDependenciesMap(aliases as Map<String, String>, project.file("build/reports/license/dependency-license.xml"))
    }

    void setupDependenciesMap(Map<String, String> aliases, File xmlPath) {
        def dependencies = new XmlParser().parse(xmlPath)
        dependencies.dependency.each {
            def filename = it.file.text() as String
            def libraryInfo = new LibraryInfo()
            if (aliases.containsKey(filename)) {
                if (!aliases[filename]) {
                    // ignore this library
                    return
                }
                libraryInfo.license = normalizeLicense(aliases[filename])
            } else {
                libraryInfo.license = normalizeLicense(it.license.@name[0] as String)
            }
            libraryInfo.artifactId = it.@name // e.g. com.google.dagger:dagger:2.0
            libraryInfo.filename = filename // e.g. dagger-2.0.jar
            dependenciesMap[filename] = libraryInfo
        }
    }

    Map<String, ?> loadYaml(File yamlFile) {
        return yaml.load(yamlFile.text) as Map<String, ?> ?: [:]
    }

    Map<String, LibraryInfo> getNotDocumented() {
        Map<String, LibraryInfo> notDocumented = [:]
        librariesMap.each { key, value ->
            if (!dependenciesMap.containsKey(key)) {
                notDocumented[key] = value
            }
        }
        return notDocumented
    }

    Map<String, LibraryInfo> getNotUsed() {
        Map<String, LibraryInfo> notUsed = [:]
        dependenciesMap.each { key, value ->
            if (!librariesMap.containsKey(key)) {
                notUsed[key] = value
            }
        }
        return notUsed
    }

    void generateLicensePage(Project project) {
        def ext = project.extensions.getByType(LicenseToolExtension)

        def noLicenseLibraries = new ArrayList<LibraryInfo>()
        def content = new StringBuilder()
        librariesMap.each { key, libraryInfo ->
            def o = dependenciesMap.get(key)
            // merge dependenciesMap's libraryInfo into librariesMap's
            if (!libraryInfo.license) {
                libraryInfo.license = o.license
            }
            libraryInfo.filename = o.filename
            libraryInfo.artifactId = o.artifactId
            try {
                content.append(Licenses.buildHtml(libraryInfo));
            } catch (NotEnoughInformationException e) {
                noLicenseLibraries.add(e.libraryInfo)
            }
        }

        if (!noLicenseLibraries.empty) {
            StringBuilder message = new StringBuilder();
            message.append("Not enough information for:\n")
            message.append("---\n")
            noLicenseLibraries.each { libraryInfo ->
                message.append("""- filename: ${libraryInfo.filename}
  license: ${libraryInfo.license ?: "#LICENSE#"}
  name: ${libraryInfo.name}
  notice: ${libraryInfo.copyrightStatement ?: "#NOTICE#"}
  year: ${libraryInfo.year}
""")
            }
            throw new RuntimeException(message.toString())
        }

        def assetsDir = project.file("src/main/assets")
        if (!assetsDir.exists()) {
            assetsDir.mkdirs()
        }

        project.file("${assetsDir}/${ext.outputHtml}").write(wrapHtml(content))
    }

    static String wrapHtml(CharSequence content) {
        return """<!DOCTYPE html>
<style>
  body {
    color: #4c4a40;
    font-size: 87.5%;
  }
  .header, .library {
    margin: 1em 0;
    padding: 0 0.5em;
    border-bottom: 1px solid #ebeae6;
  }
  .title {
    font-size: 129%;
    font-weight: bold;
    margin: 1em 0
  }
  .license {
    background-color: #f7f7f9;
    border-radius: 4px;
    border: 1px solid #e1e1e8;
    font-size: 80%;
    padding: 9px 14px;
    margin-bottom: 14px;
  }
  .license h1, .license h2 {
    font-size: 100%;
    font-weight: bold;
  }
  .license .inline {
    display: inline;
  }
  .license .block {
    margin: 1em 0;
  }
  .license pre {
    white-space: pre-wrap;
  }
  .license .low-alpha {
    list-style-type: lower-alpha;
    padding-left: 2em;
  }
</style>
$content
"""
    }

    static String normalizeLicense(String name) {
        switch (name) {
            case ~/(?i).*\bapache.*2.*/:
                return "apache2"
            case ~/(?i).*\bmit\b.*/:
                return "mit"
            case ~/(?i).*\bbsd\b.*\b2\b.*/:
                return "bsd_2_clauses"
            case ~/(?i).*\bbsd\b.*\b3\b.*/:
                return "bsd_3_clauses"
            case ~/(?i).*\bbsd\b.*\b4\b.*/:
                return "bsd_4_clauses" // not supported because it is a very legacy license
            case ~/(?i).*\bbsd\b.*/:
                return "bsd_3_clauses"
            default:
                return name
        }
    }
}
