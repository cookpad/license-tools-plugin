package com.cookpad.android.licensetool;

import java.io.File;

public class LicenseToolExtension {

    public static String NAME = "licenseTool";

    public File licensesYaml = new File("licenses.yml");

    public File licenseAliasesYaml = new File("license-aliases.yml");

    public File outputHtml = new File("licenses.html");

}
