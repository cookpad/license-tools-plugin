package com.cookpad.android.licensetools;

import org.antlr.v4.runtime.misc.OrderedHashSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DependencySet implements Iterable<LibraryInfo> {

    private final Set<LibraryInfo> set = new OrderedHashSet<>();

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

            if (a.getLicense() == null || a.getLicense().equalsIgnoreCase("no license found")) {
                continue;
            }

            for (LibraryInfo b : findAll(a.getArtifactId())) {
                if (b.isSkip()) {
                    continue;
                }

                if (b.getLicense() == null || b.getLicense().equalsIgnoreCase("no license found")) {
                    continue;
                }

                if (!a.getLicense().equalsIgnoreCase(b.getLicense())) {
                    notMatched.add(b);
                }
            }
        }
        return notMatched;
    }
}
