package com.cookpad.android.licensetools

import org.junit.Test

class DependencySetTest {

    @Test
    public void testNotListedInWithWildcardGroup() throws Exception {
        def licensesYaml = new DependencySet()
        def dependencies = new DependencySet()

        licensesYaml.add(LibraryInfo.fromYaml([
                artifact: "com.example:+:+"
        ]))
        dependencies.add(LibraryInfo.fromYaml([
                artifact: "com.example:foo:1.0.0"
        ]))
        dependencies.add(LibraryInfo.fromYaml([
                artifact: "com.example:bar:1.0.0"
        ]))
        dependencies.add(LibraryInfo.fromYaml([
                artifact: "com.example:baz:1.0.0"
        ]))

        assert dependencies.notListedIn(licensesYaml).empty
        assert licensesYaml.notListedIn(dependencies).empty
    }
    @Test

    public void testNotListedInWithWildcardGroup2() throws Exception {
        def licensesYaml = new DependencySet()
        def dependencies = new DependencySet()

        licensesYaml.add(LibraryInfo.fromYaml([
                artifact: "com.example.foo:+:+"
        ]))
        licensesYaml.add(LibraryInfo.fromYaml([
                artifact: "com.example.bar:+:+"
        ]))
        dependencies.add(LibraryInfo.fromYaml([
                artifact: "com.example.foo:foo:1.0.0",
        ]))
        dependencies.add(LibraryInfo.fromYaml([
                artifact: "com.example.bar:bar:1.0.0"
        ]))

        assert dependencies.notListedIn(licensesYaml).empty
        assert licensesYaml.notListedIn(dependencies).empty
    }


    @Test
    public void testNotListedInWithWildcardVersion() throws Exception {
        def licensesYaml = new DependencySet()
        def dependencies = new DependencySet()

        licensesYaml.add(LibraryInfo.fromYaml([
                artifact: "com.example:foo:+"
        ]))
        dependencies.add(LibraryInfo.fromYaml([
                artifact: "com.example:foo:1.0.0"
        ]))

        assert dependencies.notListedIn(licensesYaml).empty
    }


    @Test
    public void testNotListedInLibrariesYaml() throws Exception {
        def licensesYaml = new DependencySet()
        def dependencies = new DependencySet()

        licensesYaml.add(LibraryInfo.fromYaml([
                artifact: "com.example:foo:+"
        ]))
        dependencies.add(LibraryInfo.fromYaml([
                artifact: "com.example:bar:1.0.0"
        ]))

        assert dependencies.notListedIn(licensesYaml).first().artifactId.toString() == "com.example:bar:1.0.0"
    }
}
