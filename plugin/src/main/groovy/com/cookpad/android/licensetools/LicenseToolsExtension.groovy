// SPDX-License-Identifier: Apache-2.0
// Copyright (c) 2016 Cookpad Inc.

package com.cookpad.android.licensetools

public class LicenseToolsExtension {

    public static String NAME = "licenseTools"

    public File licensesYaml = new File("licenses.yml")

    public File outputHtml = new File("licenses.html")

    public File outputJson = new File("licenses.json")

    public Set<String> ignoredGroups = new HashSet<>()

    public Set<String> ignoredProjects = new HashSet<>()
}
