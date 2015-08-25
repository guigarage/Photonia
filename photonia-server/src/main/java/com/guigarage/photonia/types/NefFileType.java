package com.guigarage.photonia.types;

import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;

import java.nio.file.Path;
import java.util.Arrays;

/**
 * Created by hendrikebbers on 20.08.15.
 */
public class NefFileType implements RawFileType<NikonType2MakernoteDirectory> {

    @Override
    public Class<NikonType2MakernoteDirectory> getSpecialRawDirectoryClass() {
        return NikonType2MakernoteDirectory.class;
    }

    @Override
    public Iterable<String> getAllowedExtensions() {
        return Arrays.asList("nef", "NEF");
    }

    @Override
    public RawImageFile<NikonType2MakernoteDirectory> getRawImageFile(Path parentFolder, String name) {
        return new NefImageFile(this, parentFolder, name);
    }
}
