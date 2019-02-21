package com.cookpad.android.licensetools;

public class ArtifactId implements Comparable<ArtifactId> {

    public final String group;

    public final String name; // "+" as a wild card

    public final String version;  // "+" as a wild card

    public ArtifactId(String group, String name, String version) {
        this.group = group;
        this.name = name;
        this.version = version;
    }

    public static ArtifactId parse(String notation) {
        if (notation == null) {
            return new ArtifactId("", "", "");
        }
        String[] parts = notation.split(":");
        if (parts.length == 3) {
            return new ArtifactId(parts[0], parts[1], parts[2]);
        }
        throw new IllegalArgumentException("Invalid arguments: " + notation);
    }

    public boolean matches(ArtifactId artifactId) {
        return matchesWithWildcard(group, artifactId.group)
                && matchesWithWildcard(name, artifactId.name)
                && matchesWithWildcard(version, artifactId.version);
    }

    private boolean matchesWithWildcard(String a, String b) {
        return a.equals("+") || b.equals("+") || a.equals(b);
    }

    public String withWildcardVersion() {
        return group + ":" + name + ":+";
    }

    @Override
    public String toString() {
        return group + ":" + name + ":" + version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArtifactId artifactId = (ArtifactId) o;

        if (!group.equals(artifactId.group)) {
            return false;
        }
        if (!this.name.equals(artifactId.name)) {
            return false;
        }
        return version.equals(artifactId.version);

    }

    @Override
    public int hashCode() {
        int result = group.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }

    @Override
    public int compareTo(ArtifactId o) {
        return toString().compareToIgnoreCase(o.toString());
    }
}

