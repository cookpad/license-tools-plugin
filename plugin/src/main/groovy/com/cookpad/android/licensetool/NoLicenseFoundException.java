package com.cookpad.android.licensetool;

public class NoLicenseFoundException extends RuntimeException {
    public final LibraryInfo libraryInfo;

    public NoLicenseFoundException(LibraryInfo libraryInfo) {
        this.libraryInfo = libraryInfo;
    }
}
