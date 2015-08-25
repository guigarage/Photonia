package com.guigarage.photonia;

import org.apache.commons.io.FilenameUtils;

public interface AllowedExtensionsProvider {

    Iterable<String> getAllowedExtensions();

    default boolean isMatching(String filename) {
        return FilenameUtils.isExtension(filename, MetadataUtils.toCollection(getAllowedExtensions()));
    }
}
