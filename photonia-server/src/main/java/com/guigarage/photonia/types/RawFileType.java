package com.guigarage.photonia.types;

import com.drew.metadata.Directory;
import com.guigarage.photonia.AllowedExtensionsProvider;

import java.nio.file.Path;

public interface RawFileType<T extends Directory> extends AllowedExtensionsProvider {

    public Class<T> getSpecialRawDirectoryClass();

    public RawImageFile<T> getRawImageFile(Path parentFolder, String name);
}
