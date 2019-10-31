package com.cookpad.android.licensetools

public class LicenseToolsExtension {

    public static String NAME = "licenseTools"

    public File licensesYaml = new File("licenses.yml")

    public File outputHtml = new File("licenses.html")

    public File outputJson = new File("licenses.json")

    public Set<String> ignoredGroups = new HashSet<>()

    public Set<String> ignoredProjects = new HashSet<>()

    public String configurationRegex = /^(?!releaseUnitTest)(?:release\w*)?([cC]ompile|[cC]ompileOnly|[iI]mplementation|[aA]pi)$/

}
