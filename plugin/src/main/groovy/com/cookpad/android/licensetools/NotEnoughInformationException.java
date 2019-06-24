// SPDX-License-Identifier: Apache-2.0
// Copyright (c) 2016 Cookpad Inc.

package com.cookpad.android.licensetools;

public class NotEnoughInformationException extends RuntimeException {
    public final LibraryInfo libraryInfo;

    public NotEnoughInformationException(LibraryInfo libraryInfo) {
        this.libraryInfo = libraryInfo;
    }
}
