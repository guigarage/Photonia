package com.guigarage.photonia.types;

import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;

import java.nio.file.Path;

public class NefImageFile extends RawImageFile<NikonType2MakernoteDirectory> {

    public NefImageFile(NefFileType rawFileType, Path parentFolder, String name) {
        super(rawFileType, parentFolder, name);
    }
}
