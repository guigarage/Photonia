package com.guigarage.photonia.types;

import com.drew.metadata.Directory;

import java.nio.file.Path;

public abstract class RawImageFile<T extends Directory> extends ImageFile {

    private RawFileType<T> rawFileType;

    public RawImageFile(RawFileType<T> rawFileType, Path parentFolder, String name) {
        super(rawFileType, parentFolder, name);
        this.rawFileType = rawFileType;
    }

    @Override
    public boolean isRaw() {
        return true;
    }
}
