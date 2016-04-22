package com.cookpad.android.licensetools;

import org.antlr.v4.runtime.misc.OrderedHashSet;

import java.util.Iterator;
import java.util.Set;

public class DependencySet implements Iterable<LibraryInfo> {

    private final Set<LibraryInfo> set = new OrderedHashSet<>();

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

    public boolean contains(ArtifactId artifactId) {
        for (LibraryInfo libraryInfo : set) {
            if (libraryInfo.getArtifactId().matches(artifactId)) {
                return true;
            }
        }
        return false;
    }

    public DependencySet notListedIn(DependencySet dependencySet) {
        DependencySet notListed = new DependencySet();
        for (LibraryInfo libraryInfo : set) {
            if (!dependencySet.contains(libraryInfo.getArtifactId())) {
                notListed.add(libraryInfo);
            }
        }
        return notListed;
    }

    @Override
    public Iterator<LibraryInfo> iterator() {
        return set.iterator();
    }
}
