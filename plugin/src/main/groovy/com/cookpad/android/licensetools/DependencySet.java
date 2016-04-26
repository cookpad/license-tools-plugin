package com.cookpad.android.licensetools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DependencySet implements Iterable<LibraryInfo> {

    private final Set<LibraryInfo> set = new TreeSet<>();

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public void add(LibraryInfo libraryInfo) {
        set.add(libraryInfo);
    }

    public LibraryInfo find(ArtifactId artifactId) {
        for (LibraryInfo libraryInfo : set) {
            if (libraryInfo.getArtifactId().matches(artifactId)) {
                return libraryInfo;
            }
        }
        return null;
    }

    public List<LibraryInfo> findAll(ArtifactId artifactId) {
        List<LibraryInfo> list = new ArrayList<>();
        for (LibraryInfo libraryInfo : this) {
            if (libraryInfo.getArtifactId().matches(artifactId)) {
                list.add(libraryInfo);
            }
        }
        return list;
    }

    public boolean contains(ArtifactId artifactId) {
        for (LibraryInfo libraryInfo : this) {
            if (libraryInfo.getArtifactId().matches(artifactId)) {
                return true;
            }
        }
        return false;
    }

    public DependencySet notListedIn(DependencySet dependencySet) {
        DependencySet notListed = new DependencySet();
        for (LibraryInfo libraryInfo : this) {
            if (!dependencySet.contains(libraryInfo.getArtifactId()) && !libraryInfo.isSkip()) {
                notListed.add(libraryInfo);
            }
        }
        return notListed;
    }

    @Override
    public Iterator<LibraryInfo> iterator() {
        return set.iterator();
    }

    public DependencySet licensesNotMatched(DependencySet librariesYaml) {
        DependencySet notMatched = new DependencySet();
        for (LibraryInfo a : librariesYaml) {
            if (a.isSkip()) {
                continue;
            }

            if (a.getLicense().equals("")) {
                continue;
            }

            for (LibraryInfo b : findAll(a.getArtifactId())) {
                if (b.isSkip()) {
                    continue;
                }

                if (b.getLicense().equals("")) {
                    continue;
                }

                if (!a.getNormalizedLicense().equalsIgnoreCase(b.getNormalizedLicense())) {
                    notMatched.add(b);
                }
            }
        }
        return notMatched;
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<>();
        for (LibraryInfo libraryInfo : this) {
            list.add(libraryInfo.getArtifactId().toString());
        }
        return list.toString();
    }
}
