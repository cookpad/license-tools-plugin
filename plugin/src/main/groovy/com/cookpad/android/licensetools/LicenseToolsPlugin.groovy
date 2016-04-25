package com.cookpad.android.licensetools

import groovy.util.slurpersupport.GPathResult
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ResolvedArtifact
import org.xml.sax.helpers.DefaultHandler
import org.yaml.snakeyaml.Yaml

class LicenseToolsPlugin implements Plugin<Project> {

    final yaml = new Yaml()

    final DependencySet librariesYaml = new DependencySet() // based on libraries.yml
    final DependencySet dependencyLicenses = new DependencySet() // based on license plugin's dependency-license.xml

    @Override
    void apply(Project project) {
        project.extensions.add(LicenseToolsExtension.NAME, LicenseToolsExtension)

        project.task('checkLicenses') << {
            initialize(project)

            def notDocumented = dependencyLicenses.notListedIn(librariesYaml)
            def notInDependencies = librariesYaml.notListedIn(dependencyLicenses)
            def licensesNotMatched = dependencyLicenses.licensesNotMatched(librariesYaml)

            if (notDocumented.empty && notInDependencies.empty && licensesNotMatched.empty) {
                project.logger.info("checkLicenses: ok")
                return
            }

            LicenseToolsExtension ext = project.extensions.findByType(LicenseToolsExtension)

            if (notDocumented.size() > 0) {
                project.logger.warn("# Libraries not listed in ${ext.licensesYaml}:")
                notDocumented.each { libraryInfo ->
                    def message = new StringBuffer()
                    message.append("- artifact: ${libraryInfo.artifactId.withWildcardVersion()}\n")
                    message.append("  name: ${libraryInfo.name ?: "#NAME#"}\n")
                    message.append("  copyrightHolder: ${libraryInfo.copyrightHolder ?: "#COPYRIGHT_HOLDER#"}\n")
                    message.append("  license: ${libraryInfo.license ?: "#LICENSE#"}\n")
                    if (libraryInfo.url) {
                        message.append("  url: ${libraryInfo.url ?: "#URL#"}\n")
                    }
                    project.logger.warn(message.toString().trim())
                }
            }
            if (notInDependencies.size() > 0) {
                project.logger.warn("# Libraries listed in ${ext.licensesYaml} but not in dependencies:")
                notInDependencies.each { libraryInfo ->
                    project.logger.warn("- artifact: ${libraryInfo.artifactId}\n")
                }
            }
            if (licensesNotMatched.size() > 0) {
                project.logger.warn("# Licenses not matched with pom.xml in dependencies:")
                licensesNotMatched.each { libraryInfo ->
                    project.logger.warn("- artifact: ${libraryInfo.artifactId}\n  license: ${libraryInfo.license}")
                }
            }
            throw new GradleException("checkLicenses: missing libraries in ${ext.licensesYaml}")
        }

        def generateLicensePage = project.task('generateLicensePage') << {
            initialize(project)
            generateLicensePage(project)
        }
        generateLicensePage.dependsOn('checkLicenses')

        project.tasks.findByName("check").dependsOn('checkLicenses')
    }

    void initialize(Project project) {
        LicenseToolsExtension ext = project.extensions.findByType(LicenseToolsExtension)
        loadLibrariesYaml(project.file(ext.licensesYaml))
        loadDependencyLicenses(project)
    }

    void loadLibrariesYaml(File licensesYaml) {
        if (!licensesYaml.exists()) {
            return
        }

        def libraries = loadYaml(licensesYaml)
        for (lib in libraries) {
            def libraryInfo = LibraryInfo.fromYaml(lib)
            librariesYaml.add(libraryInfo)
        }
    }

    void loadDependencyLicenses(Project project) {
        resolveProjectDependencies(project).each { d ->
            if (d.moduleVersion.id.version == "unspecified") {
                return
            }

            def dependencyDesc = "$d.moduleVersion.id.group:$d.moduleVersion.id.name:$d.moduleVersion.id.version"

            def libraryInfo = new LibraryInfo()
            libraryInfo.artifactId = ArtifactId.parse(dependencyDesc)
            libraryInfo.filename = d.file
            dependencyLicenses.add(libraryInfo)

            Dependency pomDependency = project.dependencies.create("$dependencyDesc@pom")
            Configuration pomConfiguration = project.configurations.detachedConfiguration(pomDependency)

            pomConfiguration.resolve().each {
                project.logger.info("POM: ${it}")
            }

            File pStream
            try {
                pStream = pomConfiguration.resolve().asList().first()
            } catch (Exception e) {
                project.logger.warn("Unable to retrieve license for $dependencyDesc")
                return
            }

            XmlSlurper slurper = new XmlSlurper(true, false)
            slurper.setErrorHandler(new DefaultHandler())
            GPathResult xml = slurper.parse(pStream)

            libraryInfo.libraryName = xml.name.text()
            libraryInfo.url = xml.url.text()

            xml.licenses.license.each {
                if (!libraryInfo.license) {
                    // takes the first license
                    libraryInfo.license = it.name.text().trim()
                }
            }
        }
    }

    Map<String, ?> loadYaml(File yamlFile) {
        return yaml.load(yamlFile.text) as Map<String, ?> ?: [:]
    }

    void generateLicensePage(Project project) {
        def ext = project.extensions.getByType(LicenseToolsExtension)

        def noLicenseLibraries = new ArrayList<LibraryInfo>()
        def content = new StringBuilder()

        librariesYaml.each { libraryInfo ->
            if (libraryInfo.skip) {
                project.logger.info("generateLicensePage: skip ${libraryInfo.name}")
                return
            }

            // merge dependencyLicenses's libraryInfo into librariesYaml's
            def o = dependencyLicenses.find(libraryInfo.artifactId)
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
                message.append("- artifact: ${libraryInfo.artifactId}\n")
                message.append("  name: ${libraryInfo.name}\n")
                if (!libraryInfo.license) {
                    message.append("  license: #LICENSE#\n")
                }
                if (!libraryInfo.copyrightStatement) {
                    message.append("  copyrightHolder: #AUTHOR# (or authors: [...])\n")
                    message.append("  year: #YEAR# (optional)\n")
                }
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
<html>
  <head>
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
  </head>
  <body>
${makeIndent(content, 4)}
  </body>
</html>
"""
    }

    static String makeIndent(CharSequence content, int level) {
        def s = new StringBuilder()
        content.eachLine { line ->
            for (int i = 0; i < level; i++) {
                s.append(" ")
            }
            s.append(line)
            s.append("\n")
        }
        return s.toString()
    }

    // originated from https://github.com/hierynomus/license-gradle-plugin DependencyResolver.groovy
    Set<ResolvedArtifact> resolveProjectDependencies(Project project) {
        def dependenciesToIgnore = new HashSet<String>()

        def dependencyConfiguration = "compile"

        def dependenciesToHandle = new HashSet<ResolvedArtifact>()
        def subprojects = project.rootProject.subprojects.groupBy { Project p -> "$p.group:$p.name:$p.version" }

        def runtimeConfiguration = project.configurations.getByName(dependencyConfiguration)
        runtimeConfiguration.resolvedConfiguration.resolvedArtifacts.each { ResolvedArtifact d ->
            String dependencyDesc = "$d.moduleVersion.id.group:$d.moduleVersion.id.name:$d.moduleVersion.id.version"
            if (!dependenciesToIgnore.contains(dependencyDesc)) {
                Project subproject = subprojects[dependencyDesc]?.first()
                dependenciesToHandle.add(d)
                if (subproject) {
                    dependenciesToHandle.addAll(resolveProjectDependencies(subproject))
                }
            }
        }

        return dependenciesToHandle
    }
}
