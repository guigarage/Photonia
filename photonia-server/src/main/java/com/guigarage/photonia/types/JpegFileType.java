package com.guigarage.photonia.types;

import com.guigarage.photonia.AllowedExtensionsProvider;

import java.util.Arrays;

/**
 * Created by hendrikebbers on 20.08.15.
 */
public class JpegFileType implements AllowedExtensionsProvider {

    @Override
    public Iterable<String> getAllowedExtensions() {
        return Arrays.asList("jpg", "jpeg", "JPG", "JPEG");
    }
}