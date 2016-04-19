package com.cookpad.android.licensetool

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.yaml.snakeyaml.Yaml

class LicenseTool implements Plugin<Project> {

    Map librariesMap = [:]
    Map dependenciesMap = [:]

    void initializeMaps(String projectRootPath, List<String> modules) {
        Yaml yaml = new Yaml()
        def aliases = yaml.load(new File(projectRootPath + "/scripts/license_tools/data/aliases.yaml").text)
        def libraries = yaml.load(new File(projectRootPath + "/scripts/license_tools/data/libraries.yaml").text)
        for (lib in libraries) {
            librariesMap[lib["filename"]] = libraries
        }
        modules.each {
            setDependenciesMap(aliases as Map, projectRootPath + "/${it}/build/reports/license/dependency-license.xml")
        }
    }

    void setDependenciesMap(Map aliases, String xmlPath) {
        def dependencies = new XmlParser().parse(new File(xmlPath))
        dependencies.dependency.each {
            def fileName = it.file.text()
            def value = aliases.containsKey(fileName) ? aliases[fileName] : it.license.@name
            if (value) {
                dependenciesMap[fileName] = value
            }
        }
    }

    Map getNotDocumented() {
        Map notDocumented = [:]
        librariesMap.each {
            key, value -> if (!dependenciesMap.containsKey(key)) {
                notDocumented[key] = value
            }
        }
        return notDocumented
    }

    Map getNotUsed() {
        Map notUsed = [:]
        dependenciesMap.each {
            key, value -> if (!librariesMap.containsKey(key)) {
                notUsed[key] = value
            }
        }
        return notUsed
    }

    @Override
    void apply(Project project) {
        project.task('checkLicense') << {
            if (project.projectDir != project.rootDir) {
                return
            }
            initializeMaps(project.rootDir.getAbsoluteFile().toString(), project.childProjects.keySet().toList())
            Map notDocumented = getNotDocumented()
            Map notUsed = getNotUsed()
            if (notDocumented.size() == 0 && notUsed.size() == 0) {
                println("OK")
                return
            }
            if (notDocumented.size() > 0) {
                println("# Not documented libraries: ")
                notDocumented.each {
                    key, value -> println("- filename: ${key} \n  license: ${dependenciesMap[key]}")
                }
                println("check libraries.yaml")
            }
            if (notUsed.size() > 0) {
                println("# Not used libraries:")
                notUsed.each {
                    key, value -> println("- filename: ${key} \n  license: ${value}")
                }
                println("check aliases.yaml")
            }
            throw new GradleException("license check failed")
        }
    }

    String normalizeLicense(String name) {
        switch (name) {
            case ~/(?i)apache.*2.*/:
                return "Apache License 2.0"
            case ~/(?i)mit/:
                return "MIT"
            default:
                return name
        }
    }
}
