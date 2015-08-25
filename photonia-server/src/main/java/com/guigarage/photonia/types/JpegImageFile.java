package com.guigarage.photonia.types;

import com.guigarage.photonia.MetadataUtils;
import com.guigarage.photonia.folder.ImageFolder;

public class JpegImageFile extends RenderedImageFile {

    private ImageFolder parentImageFolder;

    public JpegImageFile(ImageFolder parentImageFolder, String name) {
        super(MetadataUtils.JPEG_FILE_TYPE, parentImageFolder.getFolderPath(), name);
        this.parentImageFolder = parentImageFolder;
    }

    public ImageFolder getParentImageFolder() {
        return parentImageFolder;
    }
}
